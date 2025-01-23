package com.example.parser.exseption;

public class DocumentFetchException extends RuntimeException {
    public DocumentFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}