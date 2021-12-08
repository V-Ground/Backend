package com.example.sources.service;

import com.example.sources.domain.dto.request.CreateInteractionRequestData;
import com.example.sources.domain.dto.request.SolveInteractionRequestData;
import com.example.sources.domain.dto.response.InteractionResponseData;
import com.example.sources.domain.dto.response.InteractionSubmitResponseData;
import com.example.sources.domain.entity.Course;
import com.example.sources.domain.entity.Interaction;
import com.example.sources.domain.entity.InteractionSubmit;
import com.example.sources.domain.entity.User;
import com.example.sources.domain.repository.course.CourseRepository;
import com.example.sources.domain.repository.courseuser.CourseUserRepository;
import com.example.sources.domain.repository.interaction.InteractionRepository;
import com.example.sources.domain.repository.interactionsubmit.InteractionSubmitRepository;
import com.example.sources.domain.repository.user.UserRepository;
import com.example.sources.domain.type.InteractionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class InteractionServiceTest {
    private static final Long TEACHER_ID = 1L;
    private static final Long STUDENT_ID = 2L;
    private static final Long COURSE_ID = 3L;
    private static final Long INTERACTION_ID = 4L;

    private final InteractionRepository interactionRepository = mock(InteractionRepository.class);
    private final InteractionSubmitRepository interactionSubmitRepository = mock(InteractionSubmitRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final CourseRepository courseRepository = mock(CourseRepository.class);
    private final CourseUserRepository courseUserRepository = mock(CourseUserRepository.class);

    private InteractionService interactionService;

    @BeforeEach
    void setUp() {
        interactionService = new InteractionService(interactionRepository,
                interactionSubmitRepository,
                userRepository,
                courseRepository,
                courseUserRepository,
                new ModelMapper());
        User teacher = User.builder()
                .id(TEACHER_ID)
                .username("Jung")
                .build();
        Course course = Course.builder()
                .teacher(teacher)
                .build();

        given(courseRepository.findById(COURSE_ID))
                .willReturn(Optional.of(course));

        given(interactionRepository.save(any(Interaction.class)))
                .willReturn(Interaction.builder().course(course).build());

        given(interactionRepository.findAllByCourseId(COURSE_ID))
                .willReturn(List.of(new InteractionResponseData(), new InteractionResponseData()));

        given(courseUserRepository.existsByCourseIdAndUserId(COURSE_ID, STUDENT_ID))
                .willReturn(true);

        given(interactionRepository.findById(INTERACTION_ID))
                .willReturn(Optional.of(Interaction.builder().course(course).build()));

        given(userRepository.findById(STUDENT_ID))
                .willReturn(Optional.of(User.builder().build()));

        given(interactionSubmitRepository.findAllByInteractionId(INTERACTION_ID))
                .willReturn(List.of(new InteractionSubmitResponseData()));
    }

    @Test
    @DisplayName("상호작용 생성 - 성공")
    void addInteraction_success() {
        CreateInteractionRequestData request = new CreateInteractionRequestData("이해 되셨나요?", InteractionType.OX);
        InteractionResponseData response = interactionService.addInteraction(COURSE_ID, request, TEACHER_ID);

        assertNotNull(response);
    }

    @Test
    @DisplayName("상호작용 조회 - 성공")
    void getInteractions_success() {
        List<InteractionResponseData> responseData = interactionService.getInteractions(COURSE_ID, TEACHER_ID);

        assertEquals(2, responseData.size());
    }

    @Test
    @DisplayName("학생 인터렉션 제출")
    void solveInteraction_success() {
        SolveInteractionRequestData requestData = new SolveInteractionRequestData(true);

        assertDoesNotThrow(() -> interactionService
                .solveInteraction(INTERACTION_ID, COURSE_ID, STUDENT_ID, requestData, STUDENT_ID));
    }

    @Test
    @DisplayName("특정 인터렉션에 대해 학생들이 제출한 모든 정답 확인 - 성공")
    void getInteractionAnswers() {
        List<InteractionSubmitResponseData> responseData = interactionService
                .getInteractionAnswers(INTERACTION_ID, COURSE_ID, TEACHER_ID);

        assertEquals(1, responseData.size());
    }
}