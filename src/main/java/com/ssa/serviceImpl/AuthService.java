//package com.ssa.serviceImpl;
//
//import com.ssa.config.Token.JwtTokenProvider;
//import com.ssa.constant.StatusConstants;
//import com.ssa.exceptions.DataNotFoundException;
//import com.ssa.model.User;
//import com.ssa.repository.UserRepository;
//import com.ssa.request.LoginRequest;
//import com.ssa.response.ApiResponse;
//import com.ssa.response.LoginResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//public class AuthService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private BCryptPasswordEncoder bCryptPE;
//
//    private final JwtTokenProvider jwtTokenProvider;
//    private final AuthenticationManager authenticationManager;
//
//    public AuthService(JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
//        this.jwtTokenProvider = jwtTokenProvider;
//        this.authenticationManager = authenticationManager;
//    }
//
//
//    public LoginResponse authenticateUser(LoginRequest loginRequest) {
//        // Authenticate the user
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
//        );
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        User user = userRepository.findByUserEmail(loginRequest.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
//
//        // Generate the JWT token
//        String token = jwtTokenProvider.generateToken(authentication);
//
//        // Prepare the login response
//        LoginResponse loginResponse = new LoginResponse();
//        loginResponse.setUserId(user.getId());  // You should get the userId dynamically
//        loginResponse.setMessage("Valid User");
//        loginResponse.setToken(token);
//
//        return loginResponse;
//    }
//}
