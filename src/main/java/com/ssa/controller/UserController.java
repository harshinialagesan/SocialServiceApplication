package com.ssa.controller;

import com.ssa.request.UserRequest;
import com.ssa.response.ApiResponse;
import com.ssa.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    UserService loginService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>> getLogin(@RequestBody UserRequest request) {
        return loginService.getLoginDetails(request);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> createUser(@RequestBody UserRequest request) {
        return loginService.createUser(request);
    }

}
