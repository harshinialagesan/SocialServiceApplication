package com.ssa.service;

import com.ssa.request.*;
import com.ssa.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<ApiResponse<Object>> getLoginDetails(LoginRequest request);

    ResponseEntity<ApiResponse<Object>> createUser(UserRequest request);

    void sendOtpForPasswordReset(String email);

    void verifyOtp(String otp);

    void resetPassword(String email, String newPassword, String confirmPassword);

    ResponseEntity<ApiResponse<Object>> updateUser(Long userId, UserUpdateRequest request);

    ResponseEntity<ApiResponse<Object>> getUserById(Long userId);

    ResponseEntity<ApiResponse<Object>> deleteUser(Long userId);


}
