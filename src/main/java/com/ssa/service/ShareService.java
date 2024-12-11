package com.ssa.service;

import com.ssa.request.SharePostRequest;
import com.ssa.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface ShareService {
    ResponseEntity<ApiResponse<Object>> sharePost(SharePostRequest sharePostRequest);

}
