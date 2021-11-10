package com.example.sources.service;

import com.example.sources.domain.dto.request.CreateEvaluationRequestData;
import com.example.sources.domain.dto.response.CreateEvaluationResponseData;
import com.example.sources.domain.entity.Evaluation;
import com.example.sources.domain.entity.User;
import com.example.sources.domain.repository.evaluation.EvaluationRepository;
import com.example.sources.domain.repository.evaluationquiz.EvaluationQuizRepository;
import com.example.sources.domain.repository.evaluationuser.EvaluationUserRepository;
import com.example.sources.domain.repository.role.RoleRepository;
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

class EvaluationServiceTest {

    private static final Long TEACHER_ID = 1L;

    private final EvaluationRepository evaluationRepository = mock(EvaluationRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final RoleRepository roleRepository = mock(RoleRepository.class);
    private final EvaluationUserRepository evaluationUserRepository = mock(EvaluationUserRepository.class);
    private final EvaluationQuizRepository evaluationQuizRepository = mock(EvaluationQuizRepository.class);

    private EvaluationService evaluationService;

    @BeforeEach
    void setUp() {
        evaluationService = new EvaluationService(evaluationRepository,
                roleRepository,
                userRepository,
                evaluationUserRepository,
                evaluationQuizRepository,
                new ModelMapper());

        User teacher = User.builder()
                .id(TEACHER_ID)
                .build();

        given(userRepository.findById(TEACHER_ID)).willReturn(Optional.of(teacher));
        given(evaluationRepository.save(any())).willReturn(new Evaluation());
    }

    @Test
    @DisplayName("테스트 생성 - 성공")
    void addEvaluation_success() {
        CreateEvaluationRequestData request = CreateEvaluationRequestData.builder()
                .userId(TEACHER_ID)
                .build();

        CreateEvaluationResponseData response = evaluationService.addEvaluation(request);

        assertNotNull(response);
    }
}