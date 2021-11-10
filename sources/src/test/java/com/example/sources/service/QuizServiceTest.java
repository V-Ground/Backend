package com.example.sources.service;

import com.example.sources.domain.dto.request.CreateQuizRequestData;
import com.example.sources.domain.dto.request.ScoringRequestData;
import com.example.sources.domain.dto.request.SolveQuizRequestData;
import com.example.sources.domain.dto.response.CreateQuizResponseData;
import com.example.sources.domain.dto.response.QuizResponseData;
import com.example.sources.domain.dto.response.SubmittedQuizResponseData;
import com.example.sources.domain.entity.Evaluation;
import com.example.sources.domain.entity.EvaluationQuiz;
import com.example.sources.domain.entity.QuizSubmit;
import com.example.sources.domain.entity.User;
import com.example.sources.domain.repository.evaluation.EvaluationRepository;
import com.example.sources.domain.repository.evaluationquiz.EvaluationQuizRepository;
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

    private QuizService quizService;

    @BeforeEach
    void setUp() {
        quizService = new QuizService(evaluationQuizRepository, quizSubmitRepository, evaluationRepository, userRepository, new ModelMapper());


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

        given(evaluationQuizRepository.findAllByEvaluationId(EVALUATION_ID)) // 모든 퀴즈 조회
                .willReturn(List.of(new QuizResponseData()));

        given(evaluationRepository.findById(EVALUATION_ID)) // 퀴즈 생성
                .willReturn(Optional.of(evaluation));

        given(userRepository.findById(STUDENT_ID)) // 정답 제출
                .willReturn(Optional.of(student));

        given(evaluationQuizRepository.findById(QUIZ_ID)) // 정답 제출
                .willReturn(Optional.of(quiz));

        given(quizSubmitRepository.findAllByEvaluationIdAndUserId(EVALUATION_ID, STUDENT_ID)) // 제출한 모든 정답 조회
                .willReturn(List.of(new SubmittedQuizResponseData(), new SubmittedQuizResponseData()));

        given(quizSubmitRepository.findById(SUBMITTED_ANSWER_ID))
                .willReturn(Optional.of(answer));
    }

    @Test
    @DisplayName("테스트에 소속된 모든 문제 조회 - 성공")
    void getAllQuizzes_success() {
        List<QuizResponseData> quizzes = quizService.getAllQuizzes(EVALUATION_ID);

        assertEquals(1, quizzes.size());
    }

    @Test
    @DisplayName("평가에 해당하는 퀴즈 생성 - 성공")
    void addEvaluationQuiz_success() {
        List<CreateQuizRequestData> request = List.of(new CreateQuizRequestData(), new CreateQuizRequestData());

        CreateQuizResponseData response = quizService.addEvaluationQuiz(EVALUATION_ID, request);

        assertEquals(2, response.getCreatedQuizNumber());
    }

    @Test
    @DisplayName("학생 문제 풀기 - 성공")
    void solveQuiz_success() {
        SolveQuizRequestData request = new SolveQuizRequestData("192.168.0.2");

        assertDoesNotThrow(() -> quizService.solveEvaluationQuiz(QUIZ_ID, request, STUDENT_ID, STUDENT_TOKEN_ID));
    }

    @Test
    @DisplayName("특정 학생이 제출한 모든 정답 확인")
    void getAllSubmittedQuizAnswer() {
        List<SubmittedQuizResponseData> all = quizService
                .getAllSubmittedQuizAnswer(TEACHER_ID, EVALUATION_ID, STUDENT_ID, TEACHER_TOKEN_ID);

        assertEquals(2, all.size());
    }

    @Test
    @DisplayName("퀴즈 채점 - 성공")
    void scoreQuiz_success() {
        ScoringRequestData request = new ScoringRequestData(100);

        assertDoesNotThrow(() -> quizService.scoreQuiz(TEACHER_ID, EVALUATION_ID, QUIZ_ID, request, TEACHER_TOKEN_ID));
    }
}