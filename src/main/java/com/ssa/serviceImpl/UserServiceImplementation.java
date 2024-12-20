package com.ssa.serviceImpl;

import com.ssa.constant.StatusConstants;
import com.ssa.exceptions.DataNotFoundException;
import com.ssa.model.OtpVerfication;
import com.ssa.model.User;
import com.ssa.repository.OtpRepository;
import com.ssa.repository.UserRepository;
import com.ssa.request.LoginRequest;
import com.ssa.request.UserRequest;
import com.ssa.request.UserUpdateRequest;
import com.ssa.response.ApiResponse;
import com.ssa.response.PostDTO;
import com.ssa.response.UserResponseDTO;
import com.ssa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


@Service
public class UserServiceImplementation implements UserService {


    public static final String PASSWORD_AND_CONFIRM_PASSWORD_DO_NOT_MATCH = "Password and Confirm Password do not match";
    public static final String USER_WITH_THIS_EMAIL_ALREADY_EXISTS = "User with this email already exists";
    public static final String USER_CREATED_SUCCESSFULLY_A_WELCOME_EMAIL_HAS_BEEN_SENT = "User created successfully. A welcome email has been sent.";

    @Autowired
    UserRepository userRepository;
    @Autowired
    OtpRepository otpRepository;

    @Autowired
    EmailService emailService;

    BCryptPasswordEncoder bCryptPE = new BCryptPasswordEncoder() ;

    @Override
    public ResponseEntity<ApiResponse<Object>> getLoginDetails(LoginRequest request) {

        User user = userRepository.findByUserEmail(request.getEmail())
                .orElseThrow(() -> new DataNotFoundException("Invalid User"));

        boolean passwordMatches = bCryptPE.matches(request.getPassword(), user.getUserPassword());
        if (!passwordMatches) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    StatusConstants.invalid(), "Invalid email or password."));
        }
        return ResponseEntity.ok(new ApiResponse<>(StatusConstants.success(), "Valid User"));
    }


    @Override
    public ResponseEntity<ApiResponse<Object>> createUser(UserRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    StatusConstants.invalid(), PASSWORD_AND_CONFIRM_PASSWORD_DO_NOT_MATCH));
        }

        if (userRepository.existsByUserEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    StatusConstants.invalid(), USER_WITH_THIS_EMAIL_ALREADY_EXISTS));
        }
        User user = new User();
        user.setUserName(request.getUserName());
        user.setUserEmail(request.getEmail());
        user.setUserPassword(bCryptPE.encode(request.getPassword()));
        user.setIsActive(request.getIsActive() != null ? request.getIsActive() : 1);

        userRepository.save(user);

        String subject = "Welcome to SSA Platform!";
        String message = "Hello " + request.getUserName() + ",\n\n"
                + "Thank you for registering on our platform. We're excited to have you on board!";
        emailService.sendWelcomeEmail(request.getEmail(), subject, message);
        return ResponseEntity.ok(new ApiResponse<>(
                StatusConstants.success(), USER_CREATED_SUCCESSFULLY_A_WELCOME_EMAIL_HAS_BEEN_SENT));
    }

    @Override
    public void sendOtpForPasswordReset(String email) {
        userRepository.findByUserEmail(email).orElseThrow(() -> new RuntimeException("User not found with this email"));

        String otp = String.valueOf(100000 + new Random().nextInt(900000));
        OtpVerfication otpVerification = new OtpVerfication();
        otpVerification.setEmail(email);
        otpVerification.setOtp(otp);
        otpVerification.setExpirationTime(LocalDateTime.now().plusSeconds(60));
        otpVerification.setVerified(0);
        otpRepository.save(otpVerification);

        String subject = "Reset Password OTP";
        String message = "Hello,\n\nYour OTP for resetting your password is: " + otp +
                "\nThis OTP is valid for 60 Seconds.";
        emailService.sendPasswordResetEmail(email, subject, message);
    }

    @Override
    public void verifyOtp(String otp) {
        OtpVerfication otpVerification = otpRepository.findByOtp(otp).orElseThrow(() -> new DataNotFoundException("Invalid OTP"));

        if (otpVerification.getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new DataNotFoundException("OTP has expired");
        }

        otpVerification.setVerified(1);
        otpRepository.save(otpVerification);
    }

    @Override
    public void resetPassword(String email, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("New password and confirm password do not match");
        }
        User user = userRepository.findByUserEmail(email).orElseThrow(() -> new RuntimeException("User not found with this email"));
        user.setUserPassword(bCryptPE.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public ResponseEntity<ApiResponse<Object>> updateUser(Long userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        if (request.getUserName() != null && !request.getUserName().isBlank()) {
            user.setUserName(request.getUserName());
        }

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            if (userRepository.existsByUserEmailAndIdNot(request.getEmail(), userId)) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), "Email already exist for this user"));
            }
            user.setUserEmail(request.getEmail());
        }
        userRepository.save(user);

        return ResponseEntity.ok(new ApiResponse<>(StatusConstants.success(), "User updated successfully"));

    }

    @Override
    public ResponseEntity<ApiResponse<Object>> getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(user.getId());
        responseDTO.setUserEmail(user.getUserEmail());
        responseDTO.setUserName(user.getUserName());
        responseDTO.setPostCount(user.getPost().size());
        List<PostDTO> postDTOs = user.getPost().stream().map(post -> {
            PostDTO postDTO = new PostDTO();
            postDTO.setId(post.getId());
            postDTO.setTitle(post.getTitle());
            postDTO.setContent(post.getDescription());
            return postDTO;
        }).collect(Collectors.toList());

        responseDTO.setPosts(postDTOs);

        return ResponseEntity.ok(new ApiResponse<>(StatusConstants.success(), responseDTO));
    }

}
