package com.ssa.controller;

import com.ssa.request.CommentRequest;
import com.ssa.response.ApiResponse;
import com.ssa.response.CommentResponse;
import com.ssa.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    @PostMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<Object>>  addComment(@PathVariable Long postId, @RequestBody CommentRequest commentRequest){
        return commentService.addComment(postId,commentRequest);
    }


    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Object>> updateComment(@PathVariable Long commentId, @RequestBody CommentRequest commentRequest) {
        return commentService.updateComment(commentId, commentRequest);
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<Object>> deleteComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestParam Long userId) {
        return commentService.deleteComment(postId, commentId, userId);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<ApiResponse<Object>> getCommentsByPost(@PathVariable Long postId) {
        return commentService.getCommentsByPost(postId);
    }
}
