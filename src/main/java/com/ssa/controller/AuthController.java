//package com.ssa.controller;
//
//import com.ssa.config.Token.JwtAuthenticationFilter;
//import com.ssa.request.AuthRequest;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/auth")
//public class AuthController {
//
//    @PostMapping("/login")
//    public String login(@RequestBody AuthRequest authRequest) {
//        // Validate username and password (use a service/repository)
//        if ("user".equals(authRequest.getUsername()) && "password".equals(authRequest.getPassword())) {
//            return JwtAuthenticationFilter.generateToken(authRequest.getUsername());
//        }
//        throw new RuntimeException("Invalid credentials");
//    }
//}
////
//
//
