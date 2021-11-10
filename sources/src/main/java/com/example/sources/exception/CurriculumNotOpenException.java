package com.example.sources.exception;

public class CurriculumNotOpenException extends RuntimeException {
    public CurriculumNotOpenException(String message) {
        super(message);
    }
}
