package com.ssa.serviceImpl;

import com.ssa.constant.StatusConstants;
import com.ssa.model.Comment;
import com.ssa.model.Post;
import com.ssa.model.User;
import com.ssa.repository.CommentRepository;
import com.ssa.repository.PostRepository;
import com.ssa.repository.UserRepository;
import com.ssa.request.CommentRequest;
import com.ssa.response.ApiResponse;
import com.ssa.response.CommentResponse;
import com.ssa.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class CommentServiceImplementation implements CommentService {

    public static final String USER_NOT_FOUND = "User Not Found";
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentRepository commentRepository;

    @Override
    public ResponseEntity<ApiResponse<Object>> addComment(Long postId, CommentRequest commentRequest) {
        Optional<User> user = userRepository.findById(commentRequest.getUserId());
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), USER_NOT_FOUND));
        }
        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), USER_NOT_FOUND));
        }

        Comment comment = new Comment();
        comment.setComment(commentRequest.getComment());
        comment.setUserId(user.get());
        comment.setPostId(post.get());
        comment.setCreatedAt(LocalDateTime.now());

        commentRepository.save(comment);

        return ResponseEntity.ok(new ApiResponse<>(StatusConstants.success(), "Comment added successfully"));

    }

    @Override
    public ResponseEntity<ApiResponse<Object>> updateComment(Long commentId, CommentRequest commentRequest) {
        Optional<Comment> existingComment = commentRepository.findById(commentId);

        if (existingComment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(StatusConstants.invalid(), "Comment not found."));
        }

        Comment comment = existingComment.get();
        Optional<User> user = userRepository.findById(commentRequest.getUserId());
        if (user.isEmpty() || !comment.getUserId().equals(user.get())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(StatusConstants.invalid(), "User is not authorized to update this comment."));
        }
        if (commentRequest.getComment() != null && !commentRequest.getComment().isEmpty()) {
            comment.setComment(commentRequest.getComment());
            comment.setUpdatedAt(LocalDateTime.now());
            commentRepository.save(comment);
            return ResponseEntity.ok(new ApiResponse<>(StatusConstants.success(), "Comment updated successfully."));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), "Comment text cannot be empty."));
        }
    }

    @Override
    public ResponseEntity<ApiResponse<Object>> deleteComment(Long postId, Long commentId, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), "User not found."));
        }
        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), "Post not found."));
        }
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(StatusConstants.invalid(), "Comment not found."));
        }

        Comment existingComment = comment.get();
        if (!existingComment.getPostId().equals(post.get())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(StatusConstants.invalid(), "Comment does not belong to the specified post."));
        }

        if (!existingComment.getUserId().equals(user.get())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(StatusConstants.invalid(), "User is not authorized to delete this comment."));
        }
        commentRepository.delete(existingComment);
        return ResponseEntity.ok(new ApiResponse<>(StatusConstants.success(), "Comment deleted successfully."));
    }

    @Override
    public ResponseEntity<ApiResponse<Object>> getCommentsByPost(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), "Post not found."));
        }
        List<Comment> comments = commentRepository.findByPostId(post.get());
        List<CommentResponse> commentResponses = comments.stream().map(this::mapToCommentResponse).collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(StatusConstants.success(), commentResponses));

    }


    private CommentResponse mapToCommentResponse(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setComment(comment.getComment());
        response.setUserName(comment.getUserId().getUserName());
        response.setUserId(comment.getUserId().getId());
        response.setPostId(comment.getPostId().getId());
        response.setCommentId(comment.getId());
        response.setCreatedAt(comment.getCreatedAt());
        response.setIsActive(comment.getIsActive());
        return response;
    }

}
