package com.ssa.controller;

import com.ssa.request.PostRequest;
import com.ssa.response.ApiResponse;
import com.ssa.response.GetAllPostResponse;
import com.ssa.response.PagedResponse;
import com.ssa.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@CrossOrigin(origins = "http://localhost:4200")
public class PostController {

    @Autowired
    PostService postService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> createPost(@Valid @RequestBody PostRequest postRequest){
        return postService.createPost(postRequest);
    }

    @PatchMapping("/update/{postId}")
    public ResponseEntity<ApiResponse<Object>> updatePost(@PathVariable Long postId,@Valid @RequestBody PostRequest postRequest) {
        return postService.updatePost(postId, postRequest);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Object>> deletePost(@PathVariable Long postId, @Valid @RequestParam Long userId) {
        return postService.deletePost(postId, userId);
    }

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<PagedResponse<GetAllPostResponse>>> getAllPosts(@RequestParam(name = "page_no", defaultValue = "0") int page,
                                                                             @RequestParam(name = "page_size",defaultValue = "10") int size,
                                                                             @RequestParam(name = "sort_by",required = false) String sortBy,
                                                                             @RequestParam(name = "tags",required = false) String tags) {
        ApiResponse<PagedResponse<GetAllPostResponse>> response = postService.getAllPosts(page, size, sortBy, tags);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/get/{postId}")
    public ResponseEntity<ApiResponse<Object>> getPostById(@PathVariable Long postId) {
        return postService.getPostById(postId);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PagedResponse<GetAllPostResponse>>> searchPosts(@RequestParam(name = "title",required = false) String title,
                                                                       @RequestParam(name = "tags",required = false) List<String> tags,
                                                                       @RequestParam(name = "page_no",defaultValue = "0") int page,
                                                                       @RequestParam(name = "page_size",defaultValue = "10") int size,
                                                                       @RequestParam(name = "sort_by",defaultValue = "createdAt") String sortBy) {
        ApiResponse<PagedResponse<GetAllPostResponse>> response = postService.searchPosts(title, tags, page, size, sortBy);
        return ResponseEntity.ok(response);
    }

}
