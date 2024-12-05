package com.ssa.config;

import com.ssa.config.Logging.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Explicit lambda-based configuration for CSRF
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/auth/**").permitAll() // Allow unauthenticated access to /auth/*
                                .anyRequest().authenticated() // Require authentication for all other requests
                )
                .addFilter(new JwtFilter()); // Add your JWT filter

        return http.build();
    }
}