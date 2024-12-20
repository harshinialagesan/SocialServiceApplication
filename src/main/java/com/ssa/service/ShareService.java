package com.ssa.service;

import com.ssa.request.SharePostRequest;
import com.ssa.response.ApiResponse;
import com.ssa.response.PagedResponse;
import com.ssa.response.SharePostResponse;
import com.ssa.response.ShareResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface ShareService {
    ResponseEntity<ApiResponse<Object>> sharePost(SharePostRequest sharePostRequest);


    ApiResponse<PagedResponse<SharePostResponse>> getAllSharedPostsByUser(Long userId, int page, int size, String sortBy);

    Page<ShareResponse> getSharedUserOfPost(Long postId, int page, int size);

}
