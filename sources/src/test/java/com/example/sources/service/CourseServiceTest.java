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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class CourseServiceTest {

    private static final Long TEACHER_ID = 1L;
    private static final Long STUDENT_ID = 2L;

    private static final Long NORMAL_COURSE = 1L;
    private static final Long DISABLE_COURSE = 2L;

    private final UserRepository userRepository = mock(UserRepository.class);
    private final CourseRepository courseRepository = mock(CourseRepository.class);

    private CourseService courseService;

    @BeforeEach
    void setUp() {
        courseService = new CourseService(
                courseRepository,
                userRepository,
                new ModelMapper());

        given(userRepository.findById(TEACHER_ID))
                .willReturn(Optional.of(User.builder().id(1L).email("teacher@gmail.com").build()));

        given(courseRepository.findById(DISABLE_COURSE))
                .willReturn(Optional.of(Course.builder().id(DISABLE_COURSE).build()));

        given(courseRepository.save(any())).will(invocation -> {
            Course course = invocation.getArgument(0);
            if(course.getId().equals(NORMAL_COURSE)) {
                return Course.builder()
                        .build();
            }else if(course.getId().equals(DISABLE_COURSE)) {
                return Course.builder()
                        .visibility(false)
                        .build();
            }

            return new Course();
        });
    }

    @Test
    @DisplayName("course 생성 성공")
    void createCourse_success() {
        CreateCourseRequestData request = new CreateCourseRequestData(
                STUDENT_ID, "와샥", "와이어샤크에 대해서 공부합니다", "1", "1", 1L);

        CreateCourseResponseData createCourseResponseData = courseService.addCourse(request);
        assertNotNull(createCourseResponseData);
    }

    @Test
    @DisplayName("course 비활성화")
    void disableCourse_success() {
        assertDoesNotThrow(() -> courseService.disableCourse(DISABLE_COURSE));
    }

}