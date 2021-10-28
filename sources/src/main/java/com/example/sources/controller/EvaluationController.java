package com.example.sources.controller;

import com.example.sources.domain.dto.request.CreateCourseRequestData;
import com.example.sources.domain.dto.request.CreateEvaluationRequestData;
import com.example.sources.domain.dto.response.CreateCourseResponseData;
import com.example.sources.domain.dto.response.CreateEvaluationResponseData;
import com.example.sources.service.AuthenticationService;
import com.example.sources.service.EvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/evaluation", produces = "application/json")
public class EvaluationController {
    private final EvaluationService evaluationService;

    @PostMapping
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('TEACHER')")
    public ResponseEntity<CreateEvaluationResponseData> create(@RequestBody CreateEvaluationRequestData request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(evaluationService.create(request));
    }

    @GetMapping("/disable/{evaluationId}")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('TEACHER')")
    public ResponseEntity disable(@PathVariable Long evaluationId) {
        evaluationService.disableEvaluation(evaluationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
