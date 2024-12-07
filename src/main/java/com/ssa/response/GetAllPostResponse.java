package com.ssa.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class GetAllPostResponse {

    private Long id;
    private String title;
    private String description;
    private Long userId;
    private List<String> tags;
    private List<String> images;
    private int likes;
    private int comments;
    private LocalDateTime createdAt;
    private String userName;
}
