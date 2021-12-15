package com.example.sources.controller;

import com.example.sources.domain.dto.request.ScoringRequestData;
import com.example.sources.domain.dto.request.SolveQuestionRequestData;
import com.example.sources.domain.dto.request.SolveQuizRequestData;
import com.example.sources.domain.dto.response.*;
import com.example.sources.security.UserAuthentication;
import com.example.sources.service.AssignmentService;
import com.example.sources.service.CourseService;
import com.example.sources.service.QuizService;
import com.example.sources.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/users", produces = "application/json")
public class UserController {
    private final UserService userService;
    private final CourseService courseService;
    private final QuizService quizService;
    private final AssignmentService assignmentService;

    @GetMapping("/{userId}/participating")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('STUDENT')")
    public ResponseEntity<MyParticipatingResponseData> getMyParticipating(@PathVariable Long userId,
                                                                          UserAuthentication authentication) {
        Long tokenUserId = authentication.getUserId();
        return ResponseEntity.ok(userService.getMyParticipating(userId, tokenUserId));
    }

    @GetMapping("/{teacherId}/courses/{courseId}")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('TEACHER')")
    public ResponseEntity<List<ParticipantResponseData>> getParticipantDetails(@PathVariable Long teacherId,
                                                                               @PathVariable Long courseId,
                                                                               UserAuthentication authentication) {
        Long tokenUserId = authentication.getUserId();
        return ResponseEntity.ok(courseService.getParticipants(courseId, tokenUserId));
    }

    @PostMapping("/{userId}/evaluations/{evaluationId}/quizzes/{quizId}")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('STUDENT')")
    public ResponseEntity solveQuiz(@PathVariable Long userId,
                                    @PathVariable Long evaluationId,
                                    @PathVariable Long quizId,
                                    @RequestBody SolveQuizRequestData request,
                                    UserAuthentication authentication) {
        Long tokenUserId = authentication.getUserId();
        quizService.solveEvaluationQuiz(userId, evaluationId, quizId, request, tokenUserId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{teacherId}/evaluations/{evaluationId}/users/{studentId}/quizzes")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('TEACHER')")
    public ResponseEntity<List<SubmittedQuizResponseData>> getStudentsQuizSubmit(@PathVariable Long teacherId,
                                                                                 @PathVariable Long evaluationId,
                                                                                 @PathVariable Long studentId,
                                                                                 UserAuthentication authentication) {
        Long tokenUserId = authentication.getUserId();
        return ResponseEntity.ok(
                quizService.getAllSubmittedQuizAnswer(teacherId, evaluationId, studentId, tokenUserId));
    }

    @PatchMapping("/{teacherId}/evaluations/{evaluationId}/answers/{answerId}")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('TEACHER')")
    public ResponseEntity scoringQuiz(@PathVariable Long teacherId,
                                                                       @PathVariable Long evaluationId,
                                                                       @PathVariable Long answerId,
                                                                       @RequestBody ScoringRequestData request,
                                                                       UserAuthentication authentication) {
        Long tokenUserId = authentication.getUserId();
        quizService.scoreQuiz(teacherId, evaluationId, answerId, request, tokenUserId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{teacherId}/courses/{courseId}/assignments/{assignmentId}/users/{userId}/answers")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('TEACHER')")
    public ResponseEntity<List<SubmittedQuestionResponseData>> getStudentQuestionSubmit(@PathVariable Long teacherId,
                                                                                        @PathVariable Long courseId,
                                                                                        @PathVariable Long assignmentId,
                                                                                        @PathVariable Long userId,
                                                                                        UserAuthentication authentication) {
        Long tokenUserId = authentication.getUserId();
        return ResponseEntity.ok(assignmentService
                .getSubmittedQuestions(teacherId, courseId, assignmentId, userId, tokenUserId));
    }

    @PostMapping("/{userId}/courses/{courseId}/assignments/{assignmentId}/questions/{questionId}/answers")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('STUDENT')")
    public ResponseEntity<SolveQuestionRequestData> solveQuestion(@PathVariable Long userId,
                                        @PathVariable Long courseId,
                                        @PathVariable Long assignmentId,
                                        @PathVariable Long questionId,
                                        @RequestBody SolveQuestionRequestData request,
                                        UserAuthentication authentication) {
        Long tokenUserId = authentication.getUserId();
        return ResponseEntity.ok(assignmentService
                .solveQuestion(userId, courseId, assignmentId, questionId, request, tokenUserId));
    }

    @PatchMapping("/{teacherId}/courses/{courseId}/assignments/{assignmentId}/questions/{questionId}/answers/{answerId}")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('TEACHER')")
    public ResponseEntity scoringQuestion(@PathVariable Long teacherId,
                                          @PathVariable Long courseId,
                                          @PathVariable Long assignmentId,
                                          @PathVariable Long questionId,
                                          @PathVariable Long answerId,
                                          @RequestBody ScoringRequestData request,
                                          UserAuthentication authentication) {
        Long tokenUserId = authentication.getUserId();
        assignmentService.scoreQuestion(teacherId, courseId, assignmentId, questionId, answerId, request, tokenUserId);
        return ResponseEntity.ok().build();
    }
}
