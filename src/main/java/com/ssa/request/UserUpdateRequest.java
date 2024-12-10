package com.ssa.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {

    @NotBlank(message = "UserName is Required")
    private String userName;
    @NotBlank(message = "Email is Required")
    private String email;
}
