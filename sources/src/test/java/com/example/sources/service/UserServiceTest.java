package com.example.sources.service;

import com.example.sources.domain.dto.response.CourseResponseData;
import com.example.sources.domain.dto.response.EvaluationResponseData;
import com.example.sources.domain.dto.response.MyParticipatingResponseData;
import com.example.sources.domain.entity.Course;
import com.example.sources.domain.entity.User;
import com.example.sources.domain.repository.course.CourseRepository;
import com.example.sources.domain.repository.courseuser.CourseUserRepository;
import com.example.sources.domain.repository.evaluationuser.EvaluationUserRepository;
import com.example.sources.domain.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class UserServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final CourseRepository courseRepository = mock(CourseRepository.class);
    private final CourseUserRepository courseUserRepository = mock(CourseUserRepository.class);
    private final EvaluationUserRepository evaluationUserRepository = mock(EvaluationUserRepository.class);

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository,
                courseRepository,
                courseUserRepository,
                evaluationUserRepository);

        given(userRepository.findById(1L))
                .willReturn(Optional.of(new User()));

        given(courseUserRepository.findAllByUserId(1L))
                .willReturn(List.of(new CourseResponseData()));

        given(evaluationUserRepository.findAllByUserId(1L))
                .willReturn(List.of(new EvaluationResponseData()));
    }

    @Test
    @DisplayName("내가 소속된 모든 과정 조회")
    void getMyParticipating() {
        MyParticipatingResponseData participating = userService.getMyParticipating(1L, 1L);

        assertAll(
                () -> assertEquals(1, participating.getEvaluation().size()),
                () -> assertEquals(1, participating.getCourse().size())
        );
    }

}