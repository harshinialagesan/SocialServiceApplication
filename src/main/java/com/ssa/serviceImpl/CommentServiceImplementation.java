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
    public static final String POST_NOT_FOUND = "Post Not Found";
    public static final String COMMENT_ADDED_SUCCESSFULLY = "Comment added successfully";
    public static final String COMMENT_NOT_FOUND = "Comment not found.";
    public static final String COMMENT_UPDATED_SUCCESSFULLY = "Comment updated successfully.";
    public static final String COMMENT_DELETED_SUCCESSFULLY = "Comment deleted successfully.";
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
            return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), POST_NOT_FOUND));
        }

        Comment comment = new Comment();
        comment.setComment(commentRequest.getComment());
        comment.setUserId(user.get());
        comment.setPostId(post.get());
        comment.setCreatedAt(LocalDateTime.now());

        commentRepository.save(comment);

        return ResponseEntity.ok(new ApiResponse<>(StatusConstants.success(), COMMENT_ADDED_SUCCESSFULLY));

    }

    @Override
    public ResponseEntity<ApiResponse<Object>> updateComment(Long commentId, CommentRequest commentRequest) {
        Optional<Comment> existingComment = commentRepository.findById(commentId);

        if (existingComment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(StatusConstants.invalid(), COMMENT_NOT_FOUND));
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
            return ResponseEntity.ok(new ApiResponse<>(StatusConstants.success(), COMMENT_UPDATED_SUCCESSFULLY));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), "Comment text cannot be empty."));
        }
    }

    @Override
    public ResponseEntity<ApiResponse<Object>> deleteComment(Long postId, Long commentId, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), USER_NOT_FOUND));
        }
        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), POST_NOT_FOUND));
        }
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(StatusConstants.invalid(), COMMENT_NOT_FOUND));
        }

        Comment existingComment = comment.get();
        if (!existingComment.getPostId().equals(post.get())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(StatusConstants.invalid(), "Comment does not belong to the specified post."));
        }

        if (!existingComment.getUserId().equals(user.get())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(StatusConstants.invalid(), "User is not authorized to delete this comment."));
        }
        commentRepository.delete(existingComment);
        return ResponseEntity.ok(new ApiResponse<>(StatusConstants.success(), COMMENT_DELETED_SUCCESSFULLY));
    }

    @Override
    public ResponseEntity<ApiResponse<Object>> getCommentsByPost(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), POST_NOT_FOUND));
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
        response.setUpdatedAt(comment.getUpdatedAt());
        response.setIsActive(comment.getIsActive());
        return response;
    }

}
