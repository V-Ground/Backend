package com.example.sources.controller;

import com.example.sources.domain.dto.request.CreateCourseRequestData;
import com.example.sources.domain.dto.response.CreateCourseResponseData;
import com.example.sources.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/courses", produces = "application/json")
public class CourseController {
    private final CourseService courseService;

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
}
