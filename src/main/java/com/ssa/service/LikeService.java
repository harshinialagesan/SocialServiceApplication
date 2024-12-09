package com.ssa.service;


import com.ssa.response.ApiResponse;
import org.springframework.http.ResponseEntity;



public interface LikeService {
    ResponseEntity<ApiResponse<Object>> likeOnPost(Long postId, Long userId);

    ResponseEntity<ApiResponse<Object>> getAllPostsLikedByUser(Long userId);
}
