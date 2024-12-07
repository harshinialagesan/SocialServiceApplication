package com.ssa.controller;

import com.ssa.request.PostRequest;
import com.ssa.response.ApiResponse;
import com.ssa.response.GetAllPostResponse;
import com.ssa.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    @Autowired
    PostService postService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> createPost(@RequestBody PostRequest postRequest){
        return postService.createPost(postRequest);
    }

    @PatchMapping("/update/{postId}")
    public ResponseEntity<ApiResponse<Object>> updatePost(@PathVariable Long postId, @RequestBody PostRequest postRequest) {
        return postService.updatePost(postId, postRequest);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Object>> deletePost(@PathVariable Long postId, @RequestParam Long userId) {
        return postService.deletePost(postId, userId);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<GetAllPostResponse>>> getAllPosts(@RequestParam(name = "page_no", defaultValue = "0") int page,
                                                                             @RequestParam(name = "page_size",defaultValue = "10") int size,
                                                                             @RequestParam(name = "sort_by",required = false) String sortBy,
                                                                             @RequestParam(name = "tags",required = false) String tags) {
        ApiResponse<List<GetAllPostResponse>> response = postService.getAllPosts(page, size, sortBy, tags);
        return ResponseEntity.ok(response);
    }


}
