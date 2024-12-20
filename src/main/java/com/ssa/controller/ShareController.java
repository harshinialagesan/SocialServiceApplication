package com.ssa.controller;

import com.ssa.constant.StatusConstants;
import com.ssa.request.SharePostRequest;
import com.ssa.response.*;
import com.ssa.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/share")
@CrossOrigin(origins = "http://localhost:4200")
public class ShareController {


   @Autowired
    ShareService shareService;


    @PostMapping("")
    public ResponseEntity<ApiResponse<Object>> sharePost(@RequestBody SharePostRequest sharePostRequest ) {
        return shareService.sharePost(sharePostRequest);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<PagedResponse<SharePostResponse>>> getAllSharedPostsByUser(@PathVariable Long userId,
                                                                                                 @RequestParam(defaultValue = "0") int page,
                                                                                                 @RequestParam(defaultValue = "10") int size,
                                                                                                 @RequestParam(required = false) String sortBy) {
        ApiResponse<PagedResponse<SharePostResponse>> response = shareService.getAllSharedPostsByUser(userId, page, size, sortBy);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<ApiResponse1<Page<ShareResponse>>> getSharedUserOfPost(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ShareResponse> share = shareService.getSharedUserOfPost(postId, page, size);
        return ResponseEntity.ok(new ApiResponse1<>(StatusConstants.success(), "Share fetched successfully", share));
    }


}
