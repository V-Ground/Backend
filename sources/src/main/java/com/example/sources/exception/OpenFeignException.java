package com.example.sources.exception;

public class OpenFeignException extends RuntimeException {
    public OpenFeignException(String message) {
        super("컨테이너와 통신중 [" + message + "] 이 발생하였습니다.");
    }
}
