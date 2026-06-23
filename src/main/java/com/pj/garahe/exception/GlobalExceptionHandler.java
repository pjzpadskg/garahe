package com.pj.garahe.exception;

import lombok.Builder;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Builder
    private record ErrorDetail(
       int status,
       String message,
       Instant timestamp
    ) {}

    private ResponseEntity<ErrorDetail> build(String message, HttpStatus status) {
        ErrorDetail detail = ErrorDetail.builder()
                .status(status.value())
                .message(message)
                .timestamp(Instant.now())
                .build();
        return new ResponseEntity<>(detail, status);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetail> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return build(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDetail> handleBadCredentialsException(BadCredentialsException ex) {
        return build(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

}
