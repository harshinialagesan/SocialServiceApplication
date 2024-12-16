package com.ssa.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse1<T> {
    private Status status;
    private String message;
    private T data;

    public ApiResponse1(Status status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ApiResponse1(Status status, String message) {
        this.status = status;
        this.message = message;
    }
}
