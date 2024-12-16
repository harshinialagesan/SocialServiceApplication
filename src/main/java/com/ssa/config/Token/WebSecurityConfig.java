    package com.ssa.config.Token;
    
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


    @Configuration
    @EnableWebSecurity
    public class WebSecurityConfig {
//        @Bean
//        protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//            http
//                    .csrf(csrf -> csrf.disable())
//                    .authorizeRequests()
//                    .requestMatchers("/user/login", "/user/create","/").permitAll()
//                    .requestMatchers("/user/**").permitAll()
//                    .requestMatchers("/post/**").permitAll()
//                    .anyRequest().permitAll() ;
////                    .and()
//////                    .formLogin(form -> form
//////                            .loginPage("/user/login")
//////                            .loginProcessingUrl("/perform_login")
//////                            .defaultSuccessUrl("/user/dashboard", true)
//////                            .failureUrl("/user/login?error=true")
//////                            .permitAll()
//////                    )
////                    .logout(logout -> logout
////                            .logoutSuccessUrl("/user/login")
////                            .invalidateHttpSession(true)
////                            .deleteCookies("JSESSIONID")
////                            .permitAll()
////                    );
//
//            return http.build();
//        }
private final JwtAuthenticationFilter jwtAuthenticationFilter;

        public WebSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
            this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/user/login", "/user/create", "/").permitAll()
                            .requestMatchers("/user/**", "/post/**").permitAll()
                            .anyRequest().authenticated()
                    )
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

            return http.build();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
            return config.getAuthenticationManager();
        }
    }
