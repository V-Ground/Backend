package com.example.sources.controller;

import com.example.sources.domain.dto.request.CreateAssignmentReqData;
import com.example.sources.domain.dto.request.CreateCourseReqData;
import com.example.sources.domain.dto.request.CreateQuestionRequestData;
import com.example.sources.domain.dto.response.*;
import com.example.sources.security.UserAuthentication;
import com.example.sources.service.AssignmentService;
import com.example.sources.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/courses", produces = "application/json")
public class CourseController {
    private final CourseService courseService;
    private final AssignmentService assignmentService;

    @PostMapping
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('TEACHER')")
    public ResponseEntity<CreateCourseResponseData> addCourse(@RequestBody CreateCourseReqData request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.addCourse(request));
    }

    @GetMapping("/{courseId}/task/status")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('TEACHER')")
    public ResponseEntity<List<TaskStatusResData>> getAllContainerStatus(@PathVariable Long courseId,
                                                                         UserAuthentication authentication) {
        Long tokenUserId = authentication.getUserId();
        return ResponseEntity.ok(courseService.getContainerStatus(courseId, tokenUserId));
    }

    @GetMapping("/disable/{courseId}")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('TEACHER')")
    public ResponseEntity<CreateCourseResponseData> disable(@PathVariable Long courseId) {
        courseService.disableCourse(courseId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{courseId}")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('TEACHER')")
    public ResponseEntity delete(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{courseId}/invite/students/{studentId}")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('TEACHER')")
    public ResponseEntity invite_need_delete_this_method(@PathVariable Long courseId,
                                                         @PathVariable Long studentId) {
        courseService.inviteStudent(courseId, studentId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{courseId}/assignments")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('TEACHER')")
    public ResponseEntity<AssignmentResponseData> addAssignment(@PathVariable Long courseId,
                                                                   @RequestBody CreateAssignmentReqData request,
                                                                   UserAuthentication authentication) {
        Long tokenUserId = authentication.getUserId();
        return ResponseEntity.status(HttpStatus.CREATED).body(assignmentService.addAssignment(courseId, request, tokenUserId));
    }

    @GetMapping("/{courseId}/assignments")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('STUDENT')")
    public ResponseEntity<List<AssignmentResponseData>> getAssignments(@PathVariable Long courseId,
                                                                      UserAuthentication authentication) {
        Long tokenUserId = authentication.getUserId();
        return ResponseEntity.ok(assignmentService.getAssignments(courseId, tokenUserId));
    }

    @PostMapping("/{courseId}/assignments/{assignmentId}")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('TEACHER')")
    public ResponseEntity<CreateQuizResponseData> addQuestion(@PathVariable Long courseId,
                                                              @PathVariable Long assignmentId,
                                                              @RequestBody List<CreateQuestionRequestData> request,
                                                              UserAuthentication authentication) {
        Long tokenUserId = authentication.getUserId();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(assignmentService.addQuestion(courseId, assignmentId, request, tokenUserId));
    }

    @GetMapping("/{courseId}/assignments/{assignmentId}/users/{userId}")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('STUDENT')")
    public ResponseEntity<QuestionWithSubmittedResponseData> getAssignment(@PathVariable Long courseId,
                                                                           @PathVariable Long assignmentId,
                                                                           @PathVariable Long userId,
                                                                           UserAuthentication authentication) {
        Long tokenUserId = authentication.getUserId();
        return ResponseEntity.ok(assignmentService.getAssignmentDetail(courseId, assignmentId, userId, tokenUserId));
    }

    @GetMapping("/{courseId}/summary")
    public ResponseEntity<AssignmentSummaryResData> getAssignmentSummary(@PathVariable Long courseId,
                                                                         UserAuthentication authentication) {
        Long tokenUserId = authentication.getUserId();
        return ResponseEntity.ok(assignmentService.getAssignmentSummary(courseId, tokenUserId));
    }
}
