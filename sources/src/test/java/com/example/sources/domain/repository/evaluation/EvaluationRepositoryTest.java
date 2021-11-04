package com.example.sources.domain.repository.evaluation;

import com.example.sources.domain.entity.Evaluation;
import com.example.sources.domain.entity.EvaluationQuiz;
import com.example.sources.domain.entity.User;
import com.example.sources.domain.repository.evaluation.EvaluationRepository;
import com.example.sources.domain.repository.evaluationquiz.EvaluationQuizRepository;
import com.example.sources.domain.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DataJpaTest
@Transactional
class EvaluationRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EvaluationRepository evaluationRepository;
    @Autowired
    private EvaluationQuizRepository evaluationQuizRepository;

    @Test
    @DisplayName("삭제")
    void delete() {
        User u = userRepository.save(User.builder().email("test@test.com").username("jang").build());
        Evaluation e = evaluationRepository.save(Evaluation.builder().teacher(u).title("평가 1").build());

        EvaluationQuiz q1 = evaluationQuizRepository.save(EvaluationQuiz.builder().answer("문제 1").score(100).evaluation(e).build());
        EvaluationQuiz q2 = evaluationQuizRepository.save(EvaluationQuiz.builder().answer("문제 2").score(100).evaluation(e).build());
        EvaluationQuiz q3 = evaluationQuizRepository.save(EvaluationQuiz.builder().answer("문제 3").score(100).evaluation(e).build());

        List<Evaluation> all = evaluationRepository.findAll();

        for (Evaluation evaluation : all) {
            System.out.println(evaluation.getTitle());
        }

        List<EvaluationQuiz> all1 = evaluationQuizRepository.findAll();
        for (EvaluationQuiz evaluationQuiz : all1) {
            System.out.println(evaluationQuiz.getAnswer());
        }

        evaluationRepository.delete(e);

        List<Evaluation> all2 = evaluationRepository.findAll();

        for (Evaluation evaluation : all2) {
            System.out.println(evaluation.getTitle());
        }

        List<EvaluationQuiz> all3 = evaluationQuizRepository.findAll();
        for (EvaluationQuiz evaluationQuiz : all3) {
            System.out.println(evaluationQuiz.getAnswer());
        }
    }

}