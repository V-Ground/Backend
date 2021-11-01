package com.example.sources.controller;

import com.example.sources.domain.dto.request.CreateAssignmentRequestData;
import com.example.sources.domain.dto.request.CreateCourseRequestData;
import com.example.sources.domain.dto.request.CreateQuestionRequestData;
import com.example.sources.domain.dto.response.AssignmentResponseData;
import com.example.sources.domain.dto.response.CreateCourseResponseData;
import com.example.sources.domain.dto.response.CreateQuizResponseData;
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
    public ResponseEntity<CreateCourseResponseData> create(@RequestBody CreateCourseRequestData request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.addCourse(request));
    }

    @GetMapping("/disable/{courseId}")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('TEACHER')")
    public ResponseEntity<CreateCourseResponseData> disable(@PathVariable Long courseId) {
        courseService.disableCourse(courseId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity delete(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{courseId}/assignments")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('TEACHER')")
    public ResponseEntity<AssignmentResponseData> addAssignment(@PathVariable Long courseId,
                                                                   @RequestBody CreateAssignmentRequestData request,
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
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('STUDENT')")
    public ResponseEntity<CreateQuizResponseData> addQuestion(@PathVariable Long courseId,
                                                              @PathVariable Long assignmentId,
                                                              @RequestBody List<CreateQuestionRequestData> request,
                                                              UserAuthentication authentication) {
        Long tokenUserId = authentication.getUserId();
        return ResponseEntity.ok(assignmentService.addQuestion(courseId, assignmentId, request, tokenUserId));
    }
}
