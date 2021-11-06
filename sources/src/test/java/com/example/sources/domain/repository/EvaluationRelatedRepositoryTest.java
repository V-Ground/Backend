package com.example.sources.domain.repository;

import com.example.sources.domain.dto.response.EvaluationResponseData;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Transactional
public class EvaluationRelatedRepositoryTest {

    @Autowired
    private EvaluationRepository evaluationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EvaluationQuizRepository evaluationQuizRepository;
    @Autowired
    private EvaluationUserRepository evaluationUserRepository;
    @Autowired
    private QuizSubmitRepository quizSubmitRepository;

    @BeforeEach
    void setUp() {
        User teacher = userRepository.save(User.builder()
                .username("kkt")
                .email("kkt@gmail.com")
                .build());
        User student = userRepository.save(User.builder()
                .username("jwi")
                .email("jwi@gmail.com")
                .build());

        Evaluation evaluation = evaluationRepository.save(Evaluation.builder()
                .title("BoB 2차 실기 평가")
                .description("BoB 2차 필기 테스트 입니다")
                .teacher(teacher)
                .build());

        evaluationUserRepository.save(EvaluationUser.builder()
                .evaluation(evaluation)
                .user(student)
                .build());

        EvaluationQuiz quiz = evaluationQuizRepository.save(EvaluationQuiz.builder()
                .question("공격자의 IP는?")
                .description("공격자의 IP를 x.x.x.x 형식으로 입력해주세요")
                .score(100)
                .evaluation(evaluation)
                .build());

        quizSubmitRepository.save(QuizSubmit.builder()
                .answer("192.168.0.2")
                .user(student)
                .evaluationQuiz(quiz)
                .build());
    }

    @Test
    @DisplayName("학생이 소속된 모든 테스트 조회")
    void findAllEvaluationByUserId() {
        List<EvaluationResponseData> all = evaluationUserRepository.findAllByUserId(10L);

        assertEquals(1, all.size());
    }

    @Test
    @DisplayName("테스트에 소속된 모든 퀴즈 조회")
    void findAllQuizByEvaluationId() {
        List<QuizResponseData> quizzes = evaluationQuizRepository.findAllByEvaluationId(1L);

        assertEquals(1, quizzes.size());
    }

    @Test
    @DisplayName("학생이 제출한 모든 정답 확인")
    void findAllSubmittedQuizByEvaluationIdAndUserId() {
        List<SubmittedQuizResponseData> all = quizSubmitRepository.findAllByEvaluationIdAndUserId(1L, 10L);

        assertEquals(1, all.size());
    }
}
