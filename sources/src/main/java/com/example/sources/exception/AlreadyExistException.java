package com.example.sources.exception;

public class AlreadyExistException extends RuntimeException {
    public AlreadyExistException(String message) {
        super("[ " + message + " ] 가 이미 존재합니다.");
    }
}
