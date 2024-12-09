package com.ssa.service;

import com.ssa.request.CommentRequest;
import com.ssa.response.ApiResponse;
import org.springframework.http.ResponseEntity;


public interface CommentService {
    ResponseEntity<ApiResponse<Object>> addComment(Long postId, CommentRequest commentRequest);

    ResponseEntity<ApiResponse<Object>> updateComment(Long commentId, CommentRequest commentRequest);

    ResponseEntity<ApiResponse<Object>> deleteComment(Long postId, Long commentId, Long userId);

    ResponseEntity<ApiResponse<Object>> getCommentsByPost(Long postId);
}
