package com.ssa.service;

import com.ssa.request.LoginRequest;
import com.ssa.request.UserRequest;
import com.ssa.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<ApiResponse<Object>> getLoginDetails(LoginRequest request);

    ResponseEntity<ApiResponse<Object>> createUser(UserRequest request);
}
