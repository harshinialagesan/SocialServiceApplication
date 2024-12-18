package com.ssa.service;

import com.ssa.request.PostRequest;
import com.ssa.response.ApiResponse;
import com.ssa.response.GetAllPostResponse;
import com.ssa.response.LikeResponse;
import com.ssa.response.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    ResponseEntity<ApiResponse<Object>> createPost(PostRequest postRequest);

    ResponseEntity<ApiResponse<Object>> updatePost(Long postId, PostRequest postRequest);

    ResponseEntity<ApiResponse<Object>> deletePost(Long postId, Long userId);

    ApiResponse<PagedResponse<GetAllPostResponse>> getAllPosts(int page, int size, String sortBy, String tags);

    ResponseEntity<ApiResponse<Object>> getPostById(Long postId);

    ApiResponse<PagedResponse<GetAllPostResponse>> searchPosts(String title, List<String> tags, int page, int size, String sortBy);


    ResponseEntity<ApiResponse<Object>> createPosts(PostRequest request, List<MultipartFile> images);

    Page<LikeResponse> getAllLikes(Long postId, int page, int size);
}
