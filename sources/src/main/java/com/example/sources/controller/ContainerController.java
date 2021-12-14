package com.example.sources.controller;

import com.example.sources.domain.dto.request.*;
import com.example.sources.domain.dto.response.ContainerBashResData;
import com.example.sources.domain.dto.response.ContainerFileResData;
import com.example.sources.domain.dto.response.ContainerInstallResData;
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

    @GetMapping("/courses/{courseId}/activation")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('TEACHER')")
    public ResponseEntity<List<ContainerStatusResponseData>> detectActivation(@PathVariable Long courseId,
                                                                           UserAuthentication authentication) {
        Long tokenUserId = authentication.getUserId();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/courses/{courseId}/installation")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('TEACHER')")
    public ResponseEntity<List<ContainerInstallResData>> detectInstallation(@PathVariable Long courseId,
                                                                            @RequestBody ContainerInstallReqData body,
                                                                            UserAuthentication authentication) {
        Long tokenUserId = authentication.getUserId();
        return ResponseEntity.ok(containerService.detectInstallation(courseId, body, tokenUserId));
    }

    @PostMapping("/courses/{courseId}/remote_command")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('TEACHER')")
    public ResponseEntity<List<ContainerBashResData>> runRemoteCommand(@PathVariable Long courseId,
                                                                       @RequestBody ContainerCommandReqData body,
                                                                       UserAuthentication authentication) {
        Long tokenUserId = authentication.getUserId();
        return ResponseEntity
                .ok(containerService.executeRemoteCommand(courseId, body, tokenUserId));
    }

    @PostMapping("/courses/{courseId}/file")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('TEACHER')")
    public ResponseEntity<List<ContainerFileResData>> getFileContent(@PathVariable Long courseId,
                                                                     @RequestBody ContainerFileReqData body,
                                                                     UserAuthentication authentication) {
        Long tokenUserId = authentication.getUserId();

        return ResponseEntity
                .ok(containerService.getFileContent(courseId, body, tokenUserId));
    }

    @PostMapping("/courses/{courseId}/bash_history/non_realtime")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('TEACHER')")
    public ResponseEntity<List<ContainerFileResData>> getBashHistory(@PathVariable Long courseId,
                                                                     @RequestBody ContainerHistoryReqData body,
                                                                     UserAuthentication authentication) {
        Long tokenUserId = authentication.getUserId();

        return ResponseEntity
                .ok(containerService.getBashHistory(courseId, body, tokenUserId));
    }

    @GetMapping
    public ResponseEntity get(@RequestParam List<String> re) {
        for (String s : re) {
            System.out.println("s = " + s);
        }
        return ResponseEntity.ok().build();
    }

}
