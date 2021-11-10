package com.example.sources.domain.repository.quizsubmit;

import com.example.sources.domain.dto.response.QuizResponseData;
import com.example.sources.domain.dto.response.SubmittedQuizResponseData;
import com.example.sources.domain.entity.EvaluationQuiz;
import com.example.sources.domain.entity.QuizSubmit;

import java.util.List;
import java.util.Optional;

public interface QuizSubmitQuery {
    List<SubmittedQuizResponseData> findAllByEvaluationIdAndUserId(Long evaluationId, Long userId);
    Optional<QuizSubmit> findByQuizIdAndUserId(Long quizId, Long userId);
}
