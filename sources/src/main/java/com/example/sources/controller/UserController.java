package com.example.sources.controller;

import com.example.sources.domain.dto.request.SolveQuizRequestData;
import com.example.sources.domain.dto.response.MyParticipatingResponseData;
import com.example.sources.security.UserAuthentication;
import com.example.sources.service.QuizService;
import com.example.sources.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/users", produces = "application/json")
public class UserController {
    private final UserService userService;
    private final QuizService quizService;

    @GetMapping("/{userId}/participating")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('USER')")
    public ResponseEntity<MyParticipatingResponseData> getMyParticipating(@PathVariable Long userId,
                                                                          UserAuthentication authentication) {
        Long tokenUserId = authentication.getUserId();
        return ResponseEntity.ok(userService.getMyParticipating(userId, tokenUserId));
    }

    @PostMapping("/{userId}/evaluations/{evaluationId}/quizzes/{quizId}")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('USER')")
    public ResponseEntity submitEvaluationQuiz(@PathVariable Long userId,
                                               @PathVariable Long evaluationId,
                                               @PathVariable Long quizId,
                                               @RequestBody SolveQuizRequestData request,
                                               UserAuthentication authentication) {
        // TODO userId 와 evaluationId 는 현재 사용하지 않지만 Validation 에 추가할 것
        Long tokenUserId = authentication.getUserId();
        quizService.solveEvaluationQuiz(quizId, request, userId, tokenUserId);
        return ResponseEntity.ok().build();
    }

}
