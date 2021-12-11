package com.example.sources.controller;

import com.example.sources.domain.dto.request.CreateInteractionReqData;
import com.example.sources.domain.dto.request.SolveInteractionRequestData;
import com.example.sources.domain.dto.response.InteractionResponseData;
import com.example.sources.domain.dto.response.InteractionSubmitResponseData;
import com.example.sources.security.UserAuthentication;
import com.example.sources.service.InteractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/interactions", produces = "application/json")
public class InteractionController {
    private final InteractionService interactionService;

    @PostMapping("/courses/{courseId}")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('TEACHER')")
    public ResponseEntity<InteractionResponseData> addInteraction(@PathVariable Long courseId,
                                                                  @RequestBody CreateInteractionReqData requestData,
                                                                  UserAuthentication authentication) {
        Long tokenUserId = authentication.getUserId();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(interactionService.addInteraction(courseId, requestData, tokenUserId));
    }

    @GetMapping("/courses/{courseId}")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('STUDENT')")
    public ResponseEntity<List<InteractionResponseData>> getInteractions(@PathVariable Long courseId,
                                                                         UserAuthentication authentication) {
        Long tokenUserId = authentication.getUserId();
        return ResponseEntity.ok(interactionService.getInteractions(courseId, tokenUserId));
    }

    @GetMapping("/{interactionId}/courses/{courseId}")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('TEACHER')")
    public ResponseEntity<List<InteractionSubmitResponseData>> getInteractionSubmit(@PathVariable Long interactionId,
                                                                                    @PathVariable Long courseId,
                                                                                    UserAuthentication authentication) {
        Long tokenUserId = authentication.getUserId();
        return ResponseEntity.ok(interactionService.getInteractionAnswers(interactionId, courseId, tokenUserId));
    }

    @PostMapping("/{interactionId}/courses/{courseId}/users/{studentId}")
    public ResponseEntity solveInteraction(@PathVariable Long interactionId,
                                           @PathVariable Long courseId,
                                           @PathVariable Long studentId,
                                           @RequestBody SolveInteractionRequestData requestData,
                                           UserAuthentication authentication) {
        Long tokenUserId = authentication.getUserId();
        interactionService.solveInteraction(interactionId, courseId, studentId, requestData, tokenUserId);
        return ResponseEntity.ok().build();
    }
}
