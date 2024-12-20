package com.ssa.serviceImpl;

import com.ssa.constant.StatusConstants;
import com.ssa.model.Likes;
import com.ssa.model.Post;
import com.ssa.model.User;
import com.ssa.repository.LikeRepository;
import com.ssa.repository.PostRepository;
import com.ssa.repository.UserRepository;
import com.ssa.response.ApiResponse;
import com.ssa.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public ResponseEntity<ApiResponse<Object>> getAllPostsLikedByUser(Long userId) {
        List<Likes> likedPosts = likeRepository.findByUserId_Id(userId);
        if (likedPosts.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), "No Post Found For The User"));
        }

        List<Post> posts = likedPosts.stream().map(Likes::getPostId).collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(StatusConstants.success(),posts));
    }
}
