package com.ssa.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserResponseDTO {

    private Long id;
    private String userEmail;
    private String userName;
    private int postCount;
    private List<PostDTO> posts;
}
