package com.example.sources.service;

import com.example.sources.domain.dto.request.CreateQuizRequestData;
import com.example.sources.domain.dto.request.ScoringRequestData;
import com.example.sources.domain.dto.request.SolveQuizRequestData;
import com.example.sources.domain.dto.response.CreateQuizResponseData;
import com.example.sources.domain.dto.response.QuizResponseData;
import com.example.sources.domain.dto.response.SubmittedQuizResponseData;
import com.example.sources.domain.entity.*;
import com.example.sources.domain.repository.evaluation.EvaluationRepository;
import com.example.sources.domain.repository.evaluationquiz.EvaluationQuizRepository;
import com.example.sources.domain.repository.evaluationuser.EvaluationUserRepository;
import com.example.sources.domain.repository.quizsubmit.QuizSubmitRepository;
import com.example.sources.domain.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class QuizServiceTest {

    private static final Long EVALUATION_ID = 1L;
    private static final Long TEACHER_ID = 1L;
    private static final Long TEACHER_TOKEN_ID = 1L;
    private static final Long STUDENT_ID = 2L;
    private static final Long STUDENT_TOKEN_ID = 2L;
    private static final Long QUIZ_ID = 1L;
    private static final Long SUBMITTED_ANSWER_ID = 1L;

    private final EvaluationQuizRepository evaluationQuizRepository = mock(EvaluationQuizRepository.class);
    private final QuizSubmitRepository quizSubmitRepository = mock(QuizSubmitRepository.class);
    private final EvaluationRepository evaluationRepository = mock(EvaluationRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final EvaluationUserRepository evaluationUserRepository = mock(EvaluationUserRepository.class);
    private QuizService quizService;

    @BeforeEach
    void setUp() {
        quizService = new QuizService(evaluationQuizRepository,
                quizSubmitRepository,
                evaluationRepository,
                userRepository,
                evaluationUserRepository,
                new ModelMapper());


        Evaluation evaluation = Evaluation.builder()
                .id(EVALUATION_ID)
                .teacher(User.builder().id(TEACHER_ID).build())
                .build();

        EvaluationQuiz quiz = EvaluationQuiz.builder()
                .id(QUIZ_ID)
                .build();

        User student = User.builder()
                .id(STUDENT_ID)
                .build();

        QuizSubmit answer = QuizSubmit.builder()
                .id(SUBMITTED_ANSWER_ID)
                .user(student)
                .build();

        given(evaluationQuizRepository.findAllByEvaluationId(EVALUATION_ID)) // ?????? ?????? ??????
                .willReturn(List.of(new QuizResponseData()));

        given(evaluationRepository.findById(EVALUATION_ID)) // ?????? ??????
                .willReturn(Optional.of(evaluation));

        given(userRepository.findById(STUDENT_ID)) // ?????? ??????
                .willReturn(Optional.of(student));

        given(evaluationQuizRepository.findById(QUIZ_ID)) // ?????? ??????
                .willReturn(Optional.of(quiz));

        given(quizSubmitRepository.findAllByEvaluationIdAndUserId(EVALUATION_ID, STUDENT_ID)) // ????????? ?????? ?????? ??????
                .willReturn(List.of(new SubmittedQuizResponseData(), new SubmittedQuizResponseData()));

        given(quizSubmitRepository.findById(SUBMITTED_ANSWER_ID))
                .willReturn(Optional.of(answer));
    }

    @Test
    @DisplayName("???????????? ????????? ?????? ?????? ?????? - ??????")
    void getAllQuizzes_success() {
        // List<QuizResponseData> quizzes = quizService.(EVALUATION_ID);

        // assertEquals(1, quizzes.size());
    }

    @Test
    @DisplayName("????????? ???????????? ?????? ?????? - ??????")
    void addEvaluationQuiz_success() {
        List<CreateQuizRequestData> request = List.of(new CreateQuizRequestData(), new CreateQuizRequestData());

        CreateQuizResponseData response = quizService.addEvaluationQuiz(EVALUATION_ID, request);

        assertEquals(2, response.getCreatedQuizNumber());
    }

    @Test
    @DisplayName("?????? ?????? ?????? - ??????")
    void solveQuiz_success() {
        SolveQuizRequestData request = new SolveQuizRequestData("192.168.0.2");

        // assertDoesNotThrow(() -> quizService.solveEvaluationQuiz(QUIZ_ID, request, STUDENT_ID, STUDENT_TOKEN_ID));
    }

    @Test
    @DisplayName("?????? ????????? ????????? ?????? ?????? ??????")
    void getAllSubmittedQuizAnswer() {
        List<SubmittedQuizResponseData> all = quizService
                .getAllSubmittedQuizAnswer(TEACHER_ID, EVALUATION_ID, STUDENT_ID, TEACHER_TOKEN_ID);

        assertEquals(2, all.size());
    }

    @Test
    @DisplayName("?????? ?????? - ??????")
    void scoreQuiz_success() {
        ScoringRequestData request = new ScoringRequestData(100);

        assertDoesNotThrow(() -> quizService.scoreQuiz(TEACHER_ID, EVALUATION_ID, QUIZ_ID, request, TEACHER_TOKEN_ID));
    }
}