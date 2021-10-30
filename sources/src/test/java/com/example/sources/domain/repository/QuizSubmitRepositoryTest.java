package com.example.sources.domain.repository;

import com.example.sources.domain.dto.response.SubmittedQuizResponseData;
import com.example.sources.domain.entity.*;
import com.example.sources.domain.repository.evaluation.EvaluationRepository;
import com.example.sources.domain.repository.evaluationquiz.EvaluationQuizRepository;
import com.example.sources.domain.repository.evaluationquiz.EvaluationQuizRepositoryImplQuery;
import com.example.sources.domain.repository.evaluationuser.EvaluationUserRepository;
import com.example.sources.domain.repository.evaluationuser.EvaluationUserRepositoryImpl;
import com.example.sources.domain.repository.quizsubmit.QuizSubmitRepository;
import com.example.sources.domain.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
class QuizSubmitRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EvaluationRepository evaluationRepository;
    @Autowired
    private EvaluationUserRepository evaluationUserRepository;
    @Autowired
    private EvaluationQuizRepository evaluationQuizRepository;
    @Autowired
    private QuizSubmitRepository quizSubmitRepository;

    @BeforeEach
    void setUp() {
        User student1 = userRepository.save(User.builder().username("jang").build());
        User student2 = userRepository.save(User.builder().username("kim").build());
        User teacher = userRepository.save(User.builder().username("lee").build());

        Evaluation evaluation = evaluationRepository.save(Evaluation.builder().teacher(teacher).build());

        evaluationUserRepository.save(EvaluationUser.builder().user(student1).evaluation(evaluation).build());
        evaluationUserRepository.save(EvaluationUser.builder().user(student2).evaluation(evaluation).build());

        EvaluationQuiz eq = evaluationQuizRepository.save(EvaluationQuiz.builder().build());

        quizSubmitRepository.save(QuizSubmit.builder().answer("정답은 123").user(student1).evaluationQuiz(eq).build());
        quizSubmitRepository.save(QuizSubmit.builder().answer("정답은 2123").user(student1).evaluationQuiz(eq).build());
        quizSubmitRepository.save(QuizSubmit.builder().answer("정답은 815123").user(student1).evaluationQuiz(eq).build());
    }

    @Test
    @DisplayName("조회")
    void get() {
        List<SubmittedQuizResponseData> allByEvaluationIdAndUserId = quizSubmitRepository.findAllByEvaluationIdAndUserId(1L, 2L);
        System.out.println("--------------");
        for (SubmittedQuizResponseData submittedQuizResponseData : allByEvaluationIdAndUserId) {
            System.out.println("submittedQuizResponseData = " + submittedQuizResponseData.toString());
        }
        System.out.println("--------------");
    }

}