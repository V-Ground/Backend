package com.example.sources.exception;

public class AwsResponseParseException extends RuntimeException {
    public AwsResponseParseException(String message) {
        super(message);
    }
}
