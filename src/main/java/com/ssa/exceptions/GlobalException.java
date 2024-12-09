package com.ssa.exceptions;

import com.ssa.constant.StatusConstants;
import com.ssa.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(InvalidTagException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidTagException(InvalidTagException ex) {
        return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), ex.getMessage()));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        if (fieldError != null) {
            String errorMessage = fieldError.getDefaultMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(StatusConstants.invalid(), errorMessage));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(StatusConstants.invalid(), "Validation error"));
    }

}
