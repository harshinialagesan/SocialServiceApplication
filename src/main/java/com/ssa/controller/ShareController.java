package com.ssa.controller;

import com.ssa.request.SharePostRequest;
import com.ssa.response.ApiResponse;
import com.ssa.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
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


}
