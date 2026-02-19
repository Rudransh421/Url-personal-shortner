package com.example.backend.exception;

public record ApiError(
        boolean success,
        int status,
        String error,
        String message,
        String timestamp,
        String path
) {
}
