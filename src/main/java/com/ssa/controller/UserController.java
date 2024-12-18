package com.ssa.controller;

import com.ssa.constant.StatusConstants;
import com.ssa.request.*;
import com.ssa.response.ApiResponse;
import com.ssa.response.GetAllPostResponse;
import com.ssa.response.PagedResponse;
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

    @PatchMapping("/update/{userId}")
    public ResponseEntity<ApiResponse<Object>> updateUser(@PathVariable Long userId,@RequestBody UserUpdateRequest request) {
        return loginService.updateUser(userId, request);
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<ApiResponse<Object>> getUserById(@PathVariable Long userId) {
        return loginService.getUserById(userId);
    }


    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ApiResponse<Object>> softDeleteUser(@PathVariable Long userId) {
       return loginService.deleteUser(userId);
    }


    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<Object>> sendOtp(@RequestBody OtpRequest request) {
        loginService.sendOtpForPasswordReset(request.getEmail());
        return ResponseEntity.ok(new ApiResponse<>(StatusConstants.success(),"OTP Sent Successfully"));
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<Object>> verifyOtp(@RequestBody OtpRequest request) {
        loginService.verifyOtp(request.getEmail(),request.getOtp());
        return ResponseEntity.ok(new ApiResponse<>(StatusConstants.success(), "OTP verified successfully"));
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<ApiResponse<Object>> resetPassword(@RequestParam("email") String email,
                                                               @RequestBody ResetPasswordRequest request) {
        try {loginService.resetPassword(email,request.getNewPassword(), request.getConfirmPassword());
            return ResponseEntity.ok(new ApiResponse<>(StatusConstants.success(),"Password reset successfully."));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(),"Could Not Reset the Password"));
        }
    }

    @GetMapping("/posts/{userId}")
    public ResponseEntity<ApiResponse<PagedResponse<GetAllPostResponse>>> getAllUserPosts(@PathVariable Long userId,
                                                                                          @RequestParam(defaultValue = "0") int page,
                                                                                          @RequestParam(defaultValue = "10") int size,
                                                                                          @RequestParam(value = "sortBy", required = false) String sortBy) {

        ApiResponse<PagedResponse<GetAllPostResponse>> response = loginService.getAllPostsByUser(userId, page, size, sortBy);
        return ResponseEntity.ok(response);
    }



}
