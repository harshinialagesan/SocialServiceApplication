package com.ssa.serviceImpl;

import com.ssa.constant.StatusConstants;
import com.ssa.model.*;
import com.ssa.repository.LikeRepository;
import com.ssa.repository.PostRepository;
import com.ssa.repository.ShareRepository;
import com.ssa.repository.UserRepository;
import com.ssa.response.ApiResponse;
import com.ssa.response.GetAllPostResponse;
import com.ssa.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class LikeServiceImplementation implements LikeService {

    public static final String POST_NOT_FOUND = "Post not found.";
    public static final String USER_NOT_FOUND = "User not found.";
    public static final String POST_LIKED_SUCCESSFULLY = "Post liked successfully.";
    public static final String POST_UNLIKED_SUCCESSFULLY = "Post unliked successfully.";
    @Autowired
    LikeRepository likeRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ShareRepository shareRepository;


    @Override
    public ResponseEntity<ApiResponse<Object>> likeOnPost(Long postId, Long userId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(StatusConstants.invalid(), POST_NOT_FOUND));
        }
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(StatusConstants.invalid(), USER_NOT_FOUND));
        }

        Post post = optionalPost.get();
        User user = optionalUser.get();
        Optional<Likes> existingLike = likeRepository.findByPostIdAndUserId(post, user);

        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            return ResponseEntity.ok(new ApiResponse<>(StatusConstants.success(), POST_UNLIKED_SUCCESSFULLY));
        } else {
            Likes like = new Likes();
            like.setPostId(post);
            like.setUserId(user);
            likeRepository.save(like);

            return ResponseEntity.ok(new ApiResponse<>(StatusConstants.success(), POST_LIKED_SUCCESSFULLY));
        }
    }

    @Override
    public ResponseEntity<ApiResponse<Object>> getAllPostsLikedByUser(Long userId, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy == null ? "createdAt" : sortBy));
        Page<Likes> likedPostsPage = likeRepository.findByUserId_Id(userId, pageable);

        if (likedPostsPage.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), "No Post Found For The User"));
        }

        List<Post> posts = likedPostsPage.stream().map(Likes::getPostId).toList();

        List<GetAllPostResponse> postResponses = posts.stream()
                .map(this::mapPostToResponses)
                .toList();

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("countOfLikedPosts", likeRepository.countByUserId_Id(userId));
        responseData.put("totalPosts", likedPostsPage.getTotalElements());
        responseData.put("totalPages", likedPostsPage.getTotalPages());
        responseData.put("currentPage", likedPostsPage.getNumber());
        responseData.put("pageSize", likedPostsPage.getSize());
        responseData.put("posts", postResponses);

        return ResponseEntity.ok(new ApiResponse<>(StatusConstants.success(), responseData));
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
