package com.ssa.serviceImpl;

import com.ssa.constant.Constants;
import com.ssa.constant.StatusConstants;
import com.ssa.exceptions.DataNotFoundException;
import com.ssa.model.*;
import com.ssa.repository.*;
import com.ssa.request.PostRequest;
import com.ssa.response.ApiResponse;
import com.ssa.response.GetAllPostResponse;
import com.ssa.response.LikeResponse;
import com.ssa.response.PagedResponse;
import com.ssa.service.PostService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostServiceImplementation implements PostService {

    public static final String USER_NOT_FOUND = "User not found.";
    public static final String POST_IS_DELETED_SUCCESSFULLY = "Post is deleted successfully";
    public static final String POST_NOT_FOUND = "Post not found";
    public static final String USER_NOT_FOUND1 = USER_NOT_FOUND;
    public static final String POST_UPDATED_SUCCESSFULLY = "Post Updated Successfully";
    public static final String POST_CREATED_SUCCESSFULLY = "Post Created Successfully";
    @Autowired
    UserRepository userRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    ShareRepository shareRepository;
    @Autowired
    LikeRepository likeRepository;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    S3Service s3Service;

    @Override
    public ResponseEntity<ApiResponse<Object>> createPost(PostRequest request) {
        Optional<User> user = userRepository.findById(request.getUserId());
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), USER_NOT_FOUND));
        }
        if (request.getTitle() == null || request.getTitle().isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), "Title is required."));
        }
        if (request.getDescription() == null || request.getDescription().isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), "Description is required."));
        }


        User user1 = user.get();
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setDescription(request.getDescription());
        post.setUserId(user1);
        post.setIsActive(Constants.IS_ACTIVE);
        List<String> tagNames = request.getTagName();
        List<Tag> tags = new ArrayList<>();
        if (tagNames != null && !tagNames.isEmpty()) {
            for (String tagName : tagNames) {
                tagName = tagName.trim();

                String[] parts = tagName.split("#");
                for (int i = 1; i < parts.length; i++) {
                    String part = parts[i].trim();
                    if (part.isEmpty()) {
                        return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), "Each tag should have a name after '#' "));
                    }
                    tagName = "#" + part;
                }

                if (!tagName.startsWith("#") || tagName.length() < 2) {
                    return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), "Each tag should start with '#' and be at least 2 characters long."));
                }
                Tag tag = tagRepository.findByName(tagName).orElse(null);
                if (tag == null) {
                    tag = new Tag();
                    tag.setName(tagName);
                    tag.setUserid(user1);
                    tag = tagRepository.save(tag);
                }
                tags.add(tag);
            }
            post.setTags(tags);
        }
        postRepository.save(post);


        return ResponseEntity.ok(new ApiResponse<>(StatusConstants.success(), POST_CREATED_SUCCESSFULLY));
    }

        @Override
        public ResponseEntity<ApiResponse<Object>> updatePost(Long postId, PostRequest postRequest,List<MultipartFile> imagesList) {
            Optional<Post> existingPost = postRepository.findById(postId);

            if (existingPost.isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), POST_NOT_FOUND));
            }

            Post post = existingPost.get();
            Optional<User> user = userRepository.findById(postRequest.getUserId());
            if (user.isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), USER_NOT_FOUND));
            }
            if (!post.getUserId().getId().equals(postRequest.getUserId())) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), "You can only update your own posts"));
            }
            if (postRequest.getTitle() != null && !postRequest.getTitle().isEmpty()) {
                post.setTitle(postRequest.getTitle());
            }
            if (postRequest.getDescription() != null && !postRequest.getDescription().isEmpty()) {
                post.setDescription(postRequest.getDescription());
            }
            List<String> tagNames = postRequest.getTagName();
            List<Tag> newTags = new ArrayList<>();

            for (String tagName : tagNames) {
                tagName = tagName.trim();

                String[] parts = tagName.split("#");
                for (int i = 1; i < parts.length; i++) {
                    String part = parts[i].trim();
                    if (part.isEmpty()) {
                        return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), "Each tag should have a name after '#' "));
                    }
                    tagName = "#" + part;
                }

                if (!tagName.startsWith("#") || tagName.length() < 2) {
                    return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), "Each tag should start with '#' and be at least 2 characters long."));
                }
                Tag tag = tagRepository.findByName(tagName).orElse(null);
                if (tag == null) {
                    tag = new Tag();
                    tag.setName(tagName);
                    tag.setUserid(user.get());
                    tag = tagRepository.save(tag);
                }
                newTags.add(tag);
            }
            List<Tag> tagsToRemove = post.getTags().stream().filter(tag -> !newTags.contains(tag)).collect(Collectors.toList());
            for (Tag tagToRemove : tagsToRemove) {
                post.getTags().remove(tagToRemove);
                boolean isTagUsedElsewhere = postRepository.existsByTagsContains(tagToRemove);

                if (!isTagUsedElsewhere) {
                    tagRepository.delete(tagToRemove);
                }
            }
            post.getTags().addAll(newTags);
            List<Images> imageList = post.getImage();
            if (imagesList != null && !imagesList.isEmpty()) {
                // 1. Validate number of images
                if (imagesList.size() + imagesList.size() > 5) {
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse<>(StatusConstants.invalid(), "You can upload a maximum of 5 images."));
                }

                // 2. Collect new image URLs by uploading the files
                Set<String> newImageUrls = new HashSet<>();
                for (MultipartFile file : imagesList) {
                    try {
                        String imageUrl = s3Service.uploadFile(file); // Upload each new image to S3
                        newImageUrls.add(imageUrl);
                    } catch (IOException e) {
                        return ResponseEntity.badRequest()
                                .body(new ApiResponse<>(StatusConstants.invalid(), "Error uploading image: " + e.getMessage()));
                    }
                }

                // 3. Remove images that are not in the new list and delete them from S3
                List<Images> imagesToRemove = new ArrayList<>();
                for (Images existingImage : imageList) {
                    if (!newImageUrls.contains(existingImage.getImageUrl())) {
                        try {
                            s3Service.deleteFile(existingImage.getImageUrl()); // Delete image from S3
                            imagesToRemove.add(existingImage); // Mark for removal from the database
                        } catch (Exception e) {
                            return ResponseEntity.badRequest()
                                    .body(new ApiResponse<>(StatusConstants.invalid(), "Error deleting image: " + e.getMessage()));
                        }
                    }
                }

                // Remove the images from the database
                imageRepository.deleteAll(imagesToRemove);
                imageList.removeAll(imagesToRemove);

                // 4. Add new images to the database
                for (String newImageUrl : newImageUrls) {
                    Images newImage = new Images();
                    newImage.setImageUrl(newImageUrl);
                    newImage.setPostId(post);
                    imageList.add(newImage);
                }
            }


            postRepository.save(post);

            return ResponseEntity.ok(new ApiResponse<>(StatusConstants.success(), POST_UPDATED_SUCCESSFULLY));
        }

    @Override
    public ResponseEntity<ApiResponse<Object>> deletePost(Long postId, Long userId) {
        Optional<Post> existingPost = postRepository.findById(postId);
        if (existingPost.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), POST_NOT_FOUND));
        }
        Post post = existingPost.get();
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), USER_NOT_FOUND1));
        }
        if (!post.getUserId().getId().equals(userId)) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), "You can only delete your own posts"));
        }
        post.setIsActive(Constants.IS_DELETED);
        postRepository.save(post);

        return ResponseEntity.ok(new ApiResponse<>(StatusConstants.success(), POST_IS_DELETED_SUCCESSFULLY));
    }

    @Override
    public ApiResponse<PagedResponse<GetAllPostResponse>> getAllPosts(int page, int size, String sortBy, String tags) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy == null ? "createdAt" : sortBy));
        Specification<Post> activeSpecification = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isActive"), 1);

        Specification<Post> tagSpecification = null;
        if (tags != null && !tags.isEmpty()) {
            List<Long> tagIds = new ArrayList<>();
            for (String tagName : tags.split(",")) {
                Tag tag = tagRepository.findByName(tagName.trim()).orElseThrow(() -> new DataNotFoundException("TagName not available"));
                tagIds.add(tag.getId());
            }
            tagSpecification = (root, query, criteriaBuilder) -> root.join("tags").get("id").in(tagIds);
        }

        Specification<Post> combinedSpecification = activeSpecification;
        if (tagSpecification != null) {
            combinedSpecification = activeSpecification.and(tagSpecification);
        }

        Page<Post> posts = postRepository.findAll(combinedSpecification, pageable);

        List<GetAllPostResponse> responses = posts.stream().map(this::mapPostToResponses).toList();
        PagedResponse<GetAllPostResponse> pagedResponse = new PagedResponse<>(
                responses,
                posts.getNumber(),
                posts.getSize(),
                posts.getTotalElements(),
                posts.getTotalPages(),
                posts.isLast()
        );
        return new ApiResponse<>(StatusConstants.success(), pagedResponse);
    }

    @Override
    public ResponseEntity<ApiResponse<Object>> getPostById(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new DataNotFoundException("Post with ID " + postId + " not found"));
        GetAllPostResponse getAllPostResponse = mapPostToResponses(post);

        return ResponseEntity.ok(new ApiResponse<>(StatusConstants.success(), getAllPostResponse));
    }

    @Override
    public ApiResponse<PagedResponse<GetAllPostResponse>> searchPosts(String title, List<String> tags, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy == null ? "createdAt" : sortBy));
        Page<Post> posts;
        if (tags != null && !tags.isEmpty()) {
            List<Long> tagId = new ArrayList<>();
            for (String tagName : tags) {
                Tag name = tagRepository.findByName(tagName.trim()).orElseThrow(() -> new DataNotFoundException("Tag '" + tagName + "' not found"));
                tagId.add(name.getId());
            }

            if (title != null && !title.isEmpty()) {
                posts = postRepository.findByTitleContainingIgnoreCaseAndTags_IdIn(title, tagId, pageable);
            } else {
                posts = postRepository.findByTags_IdIn(tagId, pageable);
            }
        } else if (title != null && !title.isEmpty()) {
            posts = postRepository.findByTitleContainingIgnoreCase(title, pageable);
        } else {
            posts = postRepository.findAll(pageable);
        }
        List<GetAllPostResponse> responses = posts.stream().map(this::mapPostToResponses).toList();

        PagedResponse<GetAllPostResponse> pagedResponse = new PagedResponse<>(
                responses,
                posts.getNumber(),
                posts.getSize(),
                posts.getTotalElements(),
                posts.getTotalPages(),
                posts.isLast()
        );
        return new ApiResponse<>(StatusConstants.success(), pagedResponse);
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse<Object>> createPosts(PostRequest request, List<MultipartFile> images) {

        Optional<User> user = userRepository.findById(request.getUserId());
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), USER_NOT_FOUND));
        }
        if (request.getTitle() == null || request.getTitle().isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), "Title is required."));
        }
        if (request.getDescription() == null || request.getDescription().isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), "Description is required."));
        }

        User user1 = user.get();
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setDescription(request.getDescription());
        post.setUserId(user1);
        post.setIsActive(Constants.IS_ACTIVE);

        if (images != null && images.size() > 5) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), "You can upload a maximum of 5 images."));
        }

        List<String> tagNames = request.getTagName();
        List<Tag> tags = new ArrayList<>();
        if (tagNames != null && !tagNames.isEmpty()) {
            for (String tagName : tagNames) {
                tagName = tagName.trim();
                if (!tagName.startsWith("#") || tagName.length() < 2) {
                    return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), "Each tag should start with '#' and be at least 2 characters long."));
                }
                Tag tag = tagRepository.findByName(tagName).orElse(null);
                if (tag == null) {
                    tag = new Tag();
                    tag.setName(tagName);
                    tag.setUserid(user1);
                    tag = tagRepository.save(tag);
                }
                tags.add(tag);
            }
            post.setTags(tags);
        }
        List<Images> imageList = new ArrayList<>();
        if (images != null && !images.isEmpty()) {
            for (MultipartFile file : images) {
                try {
                    String imageUrl = s3Service.uploadFile(file);
                    Images image = new Images();
                    image.setImageUrl(imageUrl);
                    image.setPostId(post);
                    imageList.add(image);
                } catch (IOException e) {
                    return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), "Error uploading image: " + e.getMessage()));
                }
            }
            post.setImage(imageList);
        }

        postRepository.save(post);

        return ResponseEntity.ok(new ApiResponse<>(StatusConstants.success(), POST_CREATED_SUCCESSFULLY));
    }

    @Override
    public Page<LikeResponse> getAllLikes(Long postId, int page, int size) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new DataNotFoundException("Post not found with ID: " + postId));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Likes> likes = likeRepository.findByPostId(post, pageable);

        return likes.map(like -> new LikeResponse(
                like.getUserId().getId(),
                like.getUserId().getUserName(),
                like.getCreatedAt()
        ));
    }

    @Transactional
    @Override
    public ResponseEntity<ApiResponse<Object>> deleteImageFromPost(Long postId, Long imageId) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), POST_NOT_FOUND));
        }
        Post post1 = post.get();
        Optional<Images> imageOptional = post1.getImage().stream()
                .filter(image -> image.getId().equals(imageId))
                .findFirst();

        if (imageOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), "Image not found"));
        }

        Images imageToDelete = imageOptional.get();

        try {
            s3Service.deleteFile(imageToDelete.getImageUrl());
            imageRepository.delete(imageToDelete);
            post1.getImage().remove(imageToDelete);
            postRepository.save(post1);


            return ResponseEntity.ok(new ApiResponse<>(StatusConstants.success(), "Image deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(StatusConstants.invalid(), "Error deleting image: " + e.getMessage()));
        }


    }

    private GetAllPostResponse mapPostToResponses(Post post) {
        GetAllPostResponse response = new GetAllPostResponse();
        response.setId(post.getId());
        response.setUserId(post.getUserId().getId());
        response.setTitle(post.getTitle());
        response.setDescription(post.getDescription());
        response.setTags(post.getTags().stream().map(Tag::getName).toList());
        response.setLikes(post.getLikes().size());
        response.setComments(post.getComments().size());
        response.setShare(post.getShareCount());
        Long shareCount = shareRepository.countSharesByPostId_Id(post.getId());
        post.setShareCount(shareCount);
        response.setShare(post.getShareCount());
        if (post.getImage() != null && !post.getImage().isEmpty()) {
            response.setImages(post.getImage().stream()
                    .map(Images::getImageUrl)
                    .toList());
        }
        response.setCreatedAt(post.getCreatedAt());
        response.setUserName(post.getUserId().getUserName());
        return response;
    }

}
