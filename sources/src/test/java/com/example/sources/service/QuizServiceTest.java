package com.example.sources.service;

import com.example.sources.domain.repository.evaluation.EvaluationRepository;
import com.example.sources.domain.repository.evaluationquiz.EvaluationQuizRepository;
import com.example.sources.domain.repository.evaluationuser.EvaluationUserRepository;
import com.example.sources.domain.repository.quizsubmit.QuizSubmitRepository;
import com.example.sources.domain.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class QuizServiceTest {

    private static final Long EVALUATION_ID = 1L;
    private static final Long TEACHER_ID = 1L;
    private static final Long TEACHER_TOKEN_ID = 1L;
    private static final Long STUDENT_ID = 2L;
    private static final Long STUDENT_TOKEN_ID = 2L;
    private static final Long QUIZ_ID = 1L;

    private final EvaluationQuizRepository evaluationQuizRepository = mock(EvaluationQuizRepository.class);
    private final QuizSubmitRepository quizSubmitRepository = mock(QuizSubmitRepository.class);
    private final EvaluationRepository evaluationRepository = mock(EvaluationRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);

    private QuizService quizService;

    @BeforeEach
    void setUp() {
        quizService = new QuizService(evaluationQuizRepository, quizSubmitRepository, evaluationRepository, userRepository, new ModelMapper());

        evaluationQuizRepository.findAllByEvaluationId(1L);
    }

}