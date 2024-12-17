package com.ssa.service;

import com.ssa.request.SharePostRequest;
import com.ssa.response.ApiResponse;
import com.ssa.response.PagedResponse;
import com.ssa.response.SharePostResponse;
import org.springframework.http.ResponseEntity;

public interface ShareService {
    ResponseEntity<ApiResponse<Object>> sharePost(SharePostRequest sharePostRequest);


    ApiResponse<PagedResponse<SharePostResponse>> getAllSharedPostsByUser(Long userId, int page, int size, String sortBy);
}
