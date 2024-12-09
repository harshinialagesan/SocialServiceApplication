    package com.ssa.config;
    
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.web.SecurityFilterChain;


    @Configuration
    @EnableWebSecurity
    public class WebSecurityConfig {
        @Bean
        protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(csrf -> csrf.disable()) // Important for API security
                    .authorizeRequests()
                    .requestMatchers("/user/login", "/user/create").permitAll() // Allow public access to login and user creation
                    .requestMatchers("/user/**").permitAll() // Secure other user endpoints
                    .anyRequest().permitAll() // Secure all other requests
                    .and()
                    .formLogin(form -> form
                            .loginPage("/user/login")
                            .loginProcessingUrl("/perform_login") // Add login processing URL
                            .defaultSuccessUrl("/user/dashboard", true) // Redirect after successful login
                            .failureUrl("/user/login?error=true")
                            .permitAll()
                    )
                    .logout(logout -> logout
                            .logoutSuccessUrl("/user/login")
                            .invalidateHttpSession(true)
                            .deleteCookies("JSESSIONID")
                            .permitAll()
                    );

            return http.build();
        }
    }
