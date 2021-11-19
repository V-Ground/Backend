package com.example.sources.controller;

import com.example.sources.domain.dto.request.LoginRequestData;
import com.example.sources.domain.dto.response.LoginResponseData;
import com.example.sources.security.UserAuthentication;
import com.example.sources.service.AuthenticationService;
import com.example.sources.util.CookieUtil;
import com.example.sources.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/authenticate", produces = "application/json")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final CookieUtil cookieUtil;
    private final TokenUtil tokenUtil;

    @PostMapping
    public ResponseEntity<LoginResponseData> login(
            HttpServletResponse response,
            @RequestBody LoginRequestData requestData) {

        LoginResponseData responseData = authenticationService.login(requestData);
        Long userId = responseData.getId();

        String token = tokenUtil.generateToken(userId);
        Cookie cookie = cookieUtil.generateCookie(token);
        response.addCookie(cookie);

        return ResponseEntity.ok(responseData);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('STUDENT')")
    public ResponseEntity logout(HttpServletResponse response) {
        Cookie clearCookie = cookieUtil.destroy();
        response.addCookie(clearCookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('STUDENT')")
    public ResponseEntity validate(UserAuthentication authentication) {
        Long tokenUserId = authentication.getUserId();
        return ResponseEntity.ok(authenticationService.validate(tokenUserId));
    }
}
