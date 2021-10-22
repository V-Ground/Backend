package com.example.sources.exception;

public class LoginFailedException extends RuntimeException {
    public LoginFailedException() {
        super("이메일 혹은 비밀번호를 확인해주세요.");
    }
}
