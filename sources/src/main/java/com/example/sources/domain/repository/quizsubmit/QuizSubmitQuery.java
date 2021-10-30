package com.example.sources.domain.repository.quizsubmit;

import com.example.sources.domain.dto.response.QuizResponseData;
import com.example.sources.domain.dto.response.SubmittedQuizResponseData;

import java.util.List;

public interface QuizSubmitQuery {
    List<SubmittedQuizResponseData> findAllByEvaluationIdAndUserId(Long evaluationId, Long userId);
}
