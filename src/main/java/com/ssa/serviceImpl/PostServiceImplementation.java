package com.ssa.serviceImpl;

import com.ssa.constant.Constants;
import com.ssa.constant.StatusConstants;
import com.ssa.exceptions.DataNotFoundException;
import com.ssa.model.Post;
import com.ssa.model.Tag;
import com.ssa.model.User;
import com.ssa.repository.PostRepository;
import com.ssa.repository.TagRepository;
import com.ssa.repository.UserRepository;
import com.ssa.request.PostRequest;
import com.ssa.response.ApiResponse;
import com.ssa.response.GetAllPostResponse;
import com.ssa.response.PostResponse;
import com.ssa.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImplementation implements PostService {

    public static final String USER_NOT_FOUND = "User not found.";
    public static final String POST_IS_DELETED_SUCCESSFULLY = "Post is deleted successfully";
    public static final String POST_NOT_FOUND = "Post not found";
    public static final String USER_NOT_FOUND1 = USER_NOT_FOUND;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    PostRepository postRepository;

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
        List<String> tagNames = request.getTagName();
        List<Tag> tags = new ArrayList<>();

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
        post = postRepository.save(post);
        PostResponse response = mapPostToResponse(post);

        return ResponseEntity.ok(new ApiResponse<>(StatusConstants.success(), response));
    }

    @Override
    public ResponseEntity<ApiResponse<Object>> updatePost(Long postId, PostRequest postRequest) {
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
        List<Tag> tagsToRemove = post.getTags().stream()
                .filter(tag -> !newTags.contains(tag))
                .collect(Collectors.toList());
        for (Tag tagToRemove : tagsToRemove) {
            post.getTags().remove(tagToRemove);
            boolean isTagUsedElsewhere = postRepository.existsByTagsContains(tagToRemove);

            if (!isTagUsedElsewhere) {
                tagRepository.delete(tagToRemove);
            }
        }
        post.getTags().addAll(newTags);
        post = postRepository.save(post);
        PostResponse response = mapPostToResponse(post);

        return ResponseEntity.ok(new ApiResponse<>(StatusConstants.success(), response));
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
    public ApiResponse<List<GetAllPostResponse>> getAllPosts(int page, int size, String sortBy, String tags) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy == null ? "createdAt" : sortBy));
        Page<Post> posts;

        if (tags != null && !tags.isEmpty()) {
            List<Long> tagIds = new ArrayList<>();
            for (String tagName : tags.split(",")) {
                Tag tag = tagRepository.findByName(tagName.trim()).orElseThrow(()->new DataNotFoundException("TagName not available"));
                if (tag != null) {
                    tagIds.add(tag.getId());
                }
            }
            posts = postRepository.findByTags_IdIn(tagIds, pageable);
        } else {
            posts = postRepository.findAll(pageable);
        }

        List<GetAllPostResponse> responses = posts.map(this::mapPostToResponses).toList();
        return new ApiResponse<>(StatusConstants.success(), responses);
    }



    private PostResponse mapPostToResponse(Post post) {
        PostResponse response = new PostResponse();
        response.setId(post.getId());
        response.setTitle(post.getTitle());
        response.setDescription(post.getDescription());
        response.setUserId(post.getUserId() != null ? post.getUserId().getId() : null);
        response.setTags(post.getTags() != null ? post.getTags().stream().map(Tag::getName).collect(Collectors.toList()) : new ArrayList<>());
        response.setLikes(post.getLikes() != null ? post.getLikes().size() : 0);
        response.setComments(post.getComments() != null ? post.getComments().size() : 0);
        return response;
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
//        response.setImages(post.getImage().stream().map(image -> image.getImageUrl()).toList());
        response.setCreatedAt(post.getCreatedAt());
        response.setUserName(post.getUserId().getName());
        return response;
    }

}
