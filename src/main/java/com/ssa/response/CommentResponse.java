package com.ssa.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponse {

    private Long userId;
    private Long postId;
    private Long commentId;
    private String userName;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer isActive;

}
