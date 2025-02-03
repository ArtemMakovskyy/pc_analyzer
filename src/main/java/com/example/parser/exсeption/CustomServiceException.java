package com.example.parser.exсeption;

public class CustomServiceException extends RuntimeException {
    public CustomServiceException(String message) {
        super(message);
    }

    public CustomServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
