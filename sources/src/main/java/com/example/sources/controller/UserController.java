package com.example.sources.controller;

import com.example.sources.domain.dto.response.MyParticipatingResponseData;
import com.example.sources.security.UserAuthentication;
import com.example.sources.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/users", produces = "application/json")
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}/participating")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('USER')")
    public ResponseEntity<MyParticipatingResponseData> getMyParticipating(@PathVariable Long userId,
                                                                          UserAuthentication authentication) {
        Long tokenUserId = authentication.getUserId();
        return ResponseEntity.ok(userService.getMyParticipating(userId, tokenUserId));
    }
}
