package com.ssa.service;


import com.ssa.response.ApiResponse;
import com.ssa.response.ApiResponse1;
import org.springframework.http.ResponseEntity;



public interface LikeService {
    ResponseEntity<ApiResponse1<Object>> likeOnPost(Long postId, Long userId);

    ResponseEntity<ApiResponse<Object>> getAllPostsLikedByUser(Long userId, int page, int size, String sortBy);
}
