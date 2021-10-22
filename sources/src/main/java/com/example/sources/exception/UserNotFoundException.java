package com.example.sources.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("[" + username + "] 은 존재하지 않는 회원입니다");
    }
}
