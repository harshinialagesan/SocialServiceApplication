package com.ssa.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {

    @NotNull(message = "Name is required")
    @NotBlank(message = "Name is Required")
    private String name;
    @NotNull(message = "UserName is required")
    @NotBlank(message = "UserName is Required")
    private String userName;
    @NotNull(message = "Email is required")
    @NotBlank(message = "Email is Required")
    private String email;
    @NotNull(message = "Password is required")
    @NotBlank(message = "Password is Required")
    private String password;
    @NotNull(message = "ConfirmPassword is required")
    @NotBlank(message = "ConfirmPassword is Required")
    private String confirmPassword;
    private Integer isActive;

}
