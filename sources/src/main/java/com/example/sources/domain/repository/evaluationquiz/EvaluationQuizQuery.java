package com.example.sources.domain.repository.evaluationquiz;

import com.example.sources.domain.dto.response.QuizResponseData;

import java.util.List;

public interface EvaluationQuizQuery {
    List<QuizResponseData> findAllByEvaluationId(Long evaluationId);
}
