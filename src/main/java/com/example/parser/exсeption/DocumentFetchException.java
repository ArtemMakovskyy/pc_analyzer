package com.example.parser.exсeption;

public class DocumentFetchException extends RuntimeException {
    public DocumentFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}