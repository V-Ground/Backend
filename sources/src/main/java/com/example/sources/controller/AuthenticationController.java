package com.example.sources.controller;

import com.example.sources.domain.dto.request.LoginRequestData;
import com.example.sources.domain.dto.response.LoginResponseData;
import com.example.sources.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/authenticate", produces = "application/json")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<LoginResponseData> login(
            HttpServletResponse response,
            @RequestBody LoginRequestData requestData) {
        return ResponseEntity.ok(authenticationService.login(requestData, response));
    }
}
