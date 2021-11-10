package com.example.sources.controller;

import com.example.sources.domain.dto.response.ErrorResponseData;
import com.example.sources.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseBody
@ControllerAdvice
public class ExceptionHandlerAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotFoundException.class)
    public ErrorResponseData handleNotFoundException(NotFoundException e) {
        return new ErrorResponseData(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponseData handleUserNotFoundException(UserNotFoundException e) {
        return new ErrorResponseData(e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationFailedException.class)
    public ErrorResponseData handleAuthenticationFailedException(AuthenticationFailedException e) {
        return new ErrorResponseData(e.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(LoginFailedException.class)
    public ErrorResponseData handleLoginFailedException(LoginFailedException e) {
        return new ErrorResponseData(e.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(EmptyCookieException.class)
    public ErrorResponseData handleErrorResponseData(EmptyCookieException e) {
        return new ErrorResponseData(e.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(TokenExpiredException.class)
    public ErrorResponseData handleTokenExpiredException(TokenExpiredException e) {
        return new ErrorResponseData(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CurriculumNotOpenException.class)
    public ErrorResponseData handleCurriculumNotOpenException(CurriculumNotOpenException e) {
        return new ErrorResponseData(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CurriculumClosedException.class)
    public ErrorResponseData handleCurriculumClosedException(CurriculumClosedException e) {
        return new ErrorResponseData(e.getMessage());
    }
}
