package com.ssa.service;

import com.ssa.request.LoginRequest;
import com.ssa.request.LoginRequestDto;
import com.ssa.request.LoginResponseDto;
import com.ssa.request.UserRequest;
import com.ssa.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<ApiResponse<Object>> getLoginDetails(LoginRequest request);

    ResponseEntity<ApiResponse<Object>> createUser(UserRequest request);

    void sendOtpForPasswordReset(String email);

    void verifyOtp(String email, String otp);

    void resetPasswordWithOtp(String email, String newPassword, String otp);
}
