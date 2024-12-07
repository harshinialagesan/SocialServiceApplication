package com.ssa.service;

import com.ssa.request.PostRequest;
import com.ssa.response.ApiResponse;
import com.ssa.response.GetAllPostResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PostService {
    ResponseEntity<ApiResponse<Object>> createPost(PostRequest postRequest);

    ResponseEntity<ApiResponse<Object>> updatePost(Long postId, PostRequest postRequest);

    ResponseEntity<ApiResponse<Object>> deletePost(Long postId, Long userId);

    ApiResponse<List<GetAllPostResponse>> getAllPosts(int page, int size, String sortBy, String tags);
}
