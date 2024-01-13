package com.quickbite.gateway.ExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public record ApiError (
        String path,
        String message,
        int  statusCode,
        LocalDateTime localDateTime ) {

    public ResponseEntity<ApiError> toResponse() {
        return new ResponseEntity<>(this, HttpStatus.valueOf(statusCode));
    }
}
