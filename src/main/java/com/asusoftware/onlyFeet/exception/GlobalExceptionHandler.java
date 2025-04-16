package com.asusoftware.onlyFeet.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiError> handleNotFound(NoSuchElementException ex) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleBadRequest(IllegalArgumentException ex) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex) {
        ex.printStackTrace(); // pentru debug
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Eroare internă neașteptată.");
    }

    private ResponseEntity<ApiError> buildError(HttpStatus status, String message) {
        ApiError error = new ApiError(status.value(), message, LocalDateTime.now());
        return new ResponseEntity<>(error, status);
    }

    @Data
    static class ApiError {
        private final int status;
        private final String message;
        private final LocalDateTime timestamp;
    }
}