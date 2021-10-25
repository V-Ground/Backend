package com.example.sources.exception;

public class AuthenticationFailedException extends RuntimeException {
    public AuthenticationFailedException() {
        super("인증에 실패하였습니다");
    }
}
