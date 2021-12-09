package com.example.sources.controller;

import com.example.sources.domain.dto.response.ContainerStatusResponseData;
import com.example.sources.security.UserAuthentication;
import com.example.sources.service.ContainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/containers", produces = "application/json")
public class ContainerController {
    private final ContainerService containerService;

    @GetMapping("/courses/{courseId}/exist")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('TEACHER')")
    public ResponseEntity<List<ContainerStatusResponseData>> checkFileExist(@PathVariable Long courseId,
                                                                            @RequestParam Integer searchOption,
                                                                            @RequestParam String baseDir,
                                                                            @RequestParam String filename,
                                                                            UserAuthentication authentication) {
        Long tokenUserId = authentication.getUserId();
        return ResponseEntity
                .ok(containerService.existFile(courseId, searchOption, baseDir, filename, tokenUserId));
    }

    @GetMapping("/courses/{courseId}/activation")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('TEACHER')")
    public ResponseEntity<List<ContainerStatusResponseData>> detectActivation(@PathVariable Long courseId,
                                                                           UserAuthentication authentication) {
        Long tokenUserId = authentication.getUserId();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/courses/{courseId}/installation")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('TEACHER')")
    public ResponseEntity<List<ContainerStatusResponseData>> detectInstallation(@PathVariable Long courseId,
                                                                           UserAuthentication authentication) {
        Long tokenUserId = authentication.getUserId();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/courses/{courseId}/remote_command")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('TEACHER')")
    public ResponseEntity<List<ContainerStatusResponseData>> runRemoteCommand(@PathVariable Long courseId,
                                                                                UserAuthentication authentication) {
        Long tokenUserId = authentication.getUserId();
        return ResponseEntity.ok().build();
    }
}
