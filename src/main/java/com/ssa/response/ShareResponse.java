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
public class ShareResponse {

    private Long userId;
    private String userName;
    private LocalDateTime sharedAt;
}
