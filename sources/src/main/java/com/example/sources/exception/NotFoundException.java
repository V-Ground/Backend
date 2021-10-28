package com.example.sources.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String data) {
        super("[" + data + "] 는 존재하지 않습니다");
    }
}
