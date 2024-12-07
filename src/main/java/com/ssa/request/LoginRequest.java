package com.ssa.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotNull(message = "Email is required")
    @NotBlank(message = "Email is Required")
    private String email;
    @NotNull(message = "Password is required")
    @NotBlank(message = "Password is Required")
    private String password;
}
