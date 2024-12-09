package com.ssa.controller;

import com.ssa.constant.StatusConstants;
import com.ssa.request.*;
import com.ssa.response.ApiResponse;
import com.ssa.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    UserService loginService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>> getLogin(@RequestBody LoginRequest request) {
        return loginService.getLoginDetails(request);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> createUser(@Valid  @RequestBody UserRequest request) {
        return loginService.createUser(request);
    }

//    @PostMapping("/login")
//    public ResponseEntity<LoginResponseDto> loginUser(@RequestBody LoginRequestDto loginRequestDto) {
//        LoginResponseDto loginResponseDto = loginService.loginUser(loginRequestDto);
//        return ResponseEntity.ok(loginResponseDto);
//    }

    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<Object>> sendOtp(@RequestBody OtpRequest request) {
        loginService.sendOtpForPasswordReset(request.getEmail());
        return ResponseEntity.ok(new ApiResponse<>(StatusConstants.success(),"OTP Sent Successfully"));
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<Object>> verifyOtp(@RequestBody OtpRequest request) {
        loginService.verifyOtp(request.getEmail(), request.getOtp());
        return ResponseEntity.ok(new ApiResponse<>(StatusConstants.success(), "OTP verified successfully"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Object>> resetPassword(@RequestBody ResetPasswordWithOtpRequest request) {
        loginService.resetPasswordWithOtp(request.getEmail(), request.getNewPassword(), request.getOtp());
        return ResponseEntity.ok(new ApiResponse<>(StatusConstants.success(), "Password reset successfully"));
    }

}
