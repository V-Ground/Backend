package com.example.sources.domain.repository.evaluationquiz;

import com.example.sources.domain.dto.response.QuizResponseData;
import com.example.sources.domain.entity.EvaluationQuiz;

import java.util.List;
import java.util.Optional;

public interface EvaluationQuizQuery {
    List<QuizResponseData> findAllByEvaluationId(Long evaluationId);
}
