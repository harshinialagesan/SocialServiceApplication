package com.ssa.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SharePostResponse {

    private Long id;
    private GetAllPostResponse originalPost;
    private UserDto sharedBy;
    private String comment;
    private LocalDateTime sharedAt;
}
