package com.ssa.controller;

import com.ssa.constant.StatusConstants;
import com.ssa.request.PostRequest;
import com.ssa.response.*;
import com.ssa.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(value = "/createPost",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Object>> createPost(@RequestParam("title") String title,
                                                          @RequestParam("description") String description,
                                                          @RequestParam("userId") Long userId,
                                                          @RequestParam(value = "tags", required = false) List<String> tags,
                                                          @RequestPart(value = "images", required = false) List<MultipartFile> images) {

        PostRequest request = new PostRequest();
        request.setTitle(title);
        request.setDescription(description);
        request.setUserId(userId);
        request.setTagName(tags);
        request.setImages(images);

        return postService.createPosts(request, images);
    }

    @PatchMapping("/update/{postId}")
    public ResponseEntity<ApiResponse<Object>> updatePost(@PathVariable Long postId,@Valid @RequestBody PostRequest postRequest, @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        return postService.updatePost(postId, postRequest,images);
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
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse1<Page<LikeResponse>>> getAllLikes(@PathVariable Long postId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<LikeResponse> likes = postService.getAllLikes(postId, page, size);
        return ResponseEntity.ok(new ApiResponse1<>(StatusConstants.success(), "Likes fetched successfully", likes));
    }

    @DeleteMapping("/{postId}/images/{imageId}")
    public ResponseEntity<ApiResponse<Object>> deletePostImage(@PathVariable Long postId, @PathVariable Long imageId) {
        return postService.deleteImageFromPost(postId, imageId);
    }


}
