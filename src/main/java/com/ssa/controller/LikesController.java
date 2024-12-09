package com.ssa.controller;

import com.ssa.response.ApiResponse;
import com.ssa.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/likes")
@CrossOrigin(origins = "http://localhost:4200")
public class LikesController {

    @Autowired
    LikeService likeService;

    @PostMapping("/post/{postId}")
    public ResponseEntity<ApiResponse<Object>> likeOnPost(@PathVariable Long postId, @RequestParam Long userId) {
        return likeService.likeOnPost(postId, userId);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Object>> getAllPostsLikedByUser(@PathVariable Long userId) {
        return likeService.getAllPostsLikedByUser(userId);
    }



}
