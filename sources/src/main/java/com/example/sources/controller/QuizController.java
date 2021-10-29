package com.example.sources.controller;

import com.example.sources.domain.dto.request.CreateQuizRequestData;
import com.example.sources.domain.dto.response.CreateQuizResponseData;
import com.example.sources.domain.dto.response.QuizResponseData;
import com.example.sources.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/quizzes", produces = "application/json")
public class QuizController {
    private final QuizService quizService;

    @PostMapping("/evaluation/{evaluationId}")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('TEACHER')")
    public ResponseEntity<CreateQuizResponseData> addEvaluationQuiz(@PathVariable Long evaluationId,
                                                                    @RequestBody List<CreateQuizRequestData> request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(quizService.addEvaluationQuiz(evaluationId, request));
    }

    @GetMapping("/evaluation/{evaluationId}")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('USER')")
    public ResponseEntity<List<QuizResponseData>> getAllEvaluationQuiz(@PathVariable Long evaluationId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(quizService.getAllQuizzes(evaluationId));
    }
}
