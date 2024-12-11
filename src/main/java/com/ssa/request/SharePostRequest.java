package com.ssa.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SharePostRequest {

    private Long userId;
    private Long postId;
    private String comment;

}
