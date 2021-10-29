package com.example.sources.domain.repository.evaluationquiz;

import com.example.sources.domain.entity.EvaluationQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationQuizRepository extends JpaRepository<EvaluationQuiz, Long>, EvaluationQuizQuery {
}
