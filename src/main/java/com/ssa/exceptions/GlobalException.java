package com.ssa.exceptions;

import com.ssa.constant.StatusConstants;
import com.ssa.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(InvalidTagException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidTagException(InvalidTagException ex) {
        return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), ex.getMessage()));
    }


}
