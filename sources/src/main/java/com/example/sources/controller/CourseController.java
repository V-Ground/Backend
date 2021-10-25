package com.example.sources.controller;

import com.example.sources.domain.dto.request.CreateCourseRequestData;
import com.example.sources.domain.dto.response.CreateCourseResponseData;
import com.example.sources.security.UserAuthentication;
import com.example.sources.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/course", produces = "application/json")
public class CourseController {
    private final CourseService courseService;

    @PostMapping
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('TEACHER')")
    public ResponseEntity<CreateCourseResponseData> create(@RequestBody CreateCourseRequestData request,
                                                           UserAuthentication authentication) {
        Long tokenUserId = authentication.getUserid();
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.create(request, tokenUserId));
    }
}
