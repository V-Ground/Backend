package com.example.sources.controller;

import com.example.sources.domain.dto.request.CreateEvaluationRequestData;
import com.example.sources.domain.dto.request.CreateQuizRequestData;
import com.example.sources.domain.dto.response.CreateEvaluationResponseData;
import com.example.sources.domain.dto.response.CreateQuizResponseData;
import com.example.sources.domain.dto.response.QuizResponseData;
import com.example.sources.service.EvaluationService;
import com.example.sources.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/evaluations", produces = "application/json")
public class EvaluationController {
    private final EvaluationService evaluationService;
    private final QuizService quizService;

    @PostMapping
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('TEACHER')")
    public ResponseEntity<CreateEvaluationResponseData> create(@RequestBody CreateEvaluationRequestData request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(evaluationService.addEvaluation(request));
    }

    @PatchMapping("/{evaluationId}/disable")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('TEACHER')")
    public ResponseEntity disable(@PathVariable Long evaluationId) {
        evaluationService.disableEvaluation(evaluationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{evaluationId}")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('TEACHER')")
    public ResponseEntity delete(@PathVariable Long evaluationId) {
        evaluationService.deleteEvaluation(evaluationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{evaluationId}/quizzes")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('TEACHER')")
    public ResponseEntity<CreateQuizResponseData> addEvaluationQuiz(@PathVariable Long evaluationId,
                                                                    @RequestBody List<CreateQuizRequestData> request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(quizService.addEvaluationQuiz(evaluationId, request));
    }

    @GetMapping("/{evaluationId}/quizzes")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('USER')")
    public ResponseEntity<List<QuizResponseData>> getAllEvaluationQuiz(@PathVariable Long evaluationId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(quizService.getAllQuizzes(evaluationId));
    }
}
