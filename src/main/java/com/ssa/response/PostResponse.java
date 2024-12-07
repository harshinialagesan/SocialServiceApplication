package com.ssa.response;

import com.ssa.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostResponse {

    private Long id;
    private String title;
    private String description;
    private Long userId;
    private List<String> tags;
    private List<String> images;
    private int likes;
    private int comments;

}
