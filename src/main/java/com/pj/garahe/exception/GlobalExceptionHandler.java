package com.pj.garahe.exception;

import lombok.Builder;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

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


    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ErrorDetail> handleResourceConflictException(ResourceConflictException ex) {
        return build(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorDetail> handleUnauthorizedException(UnauthorizedException ex) {
        return build(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetail> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return build("Some fields are invalid/missing. Check your input and try again.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorDetail> handleNoResourceFoundException(NoResourceFoundException ex) {
        return build(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<ErrorDetail> handleInvalidDataAccessApiUsageException(InvalidDataAccessApiUsageException ex) {
        return build(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetail> handleMethodArgumentNotValidException(Exception ex) {
        return build("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
