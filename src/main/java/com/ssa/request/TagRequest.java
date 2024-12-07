package com.ssa.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TagRequest {

    private List<String> tagName;

    private Long userId;

}
