package com.example.sources.service;

import com.example.sources.domain.dto.request.CreateCourseRequestData;
import com.example.sources.domain.dto.response.CreateCourseResponseData;
import com.example.sources.domain.entity.Course;
import com.example.sources.domain.entity.User;
import com.example.sources.domain.repository.course.CourseRepository;
import com.example.sources.domain.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class CourseServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final CourseRepository courseRepository = mock(CourseRepository.class);

    private CourseService courseService;

    @BeforeEach
    void setUp() {
        courseService = new CourseService(
                courseRepository,
                userRepository,
                new ModelMapper());

        given(userRepository.findById(1L))
                .willReturn(Optional.of(new User()));

        given(courseRepository.save(any()))
                .willReturn(new Course());
    }

    @Test
    @DisplayName("course 생성 성공")
    void createCourse_success() {
        CreateCourseRequestData request = new CreateCourseRequestData(
                1L, "와샥", "와이어샤크에 대해서 공부합니다", "1", "1", 1L);

        CreateCourseResponseData createCourseResponseData = courseService.create(request, 1L);
        assertNotNull(createCourseResponseData);
    }

}