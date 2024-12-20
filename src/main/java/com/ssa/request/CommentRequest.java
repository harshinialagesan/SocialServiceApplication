package com.ssa.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {
    @NotNull(message = "User ID is required")
    private Long userId;
    @NotBlank(message = "Comment is required")
    private String comment;
}
