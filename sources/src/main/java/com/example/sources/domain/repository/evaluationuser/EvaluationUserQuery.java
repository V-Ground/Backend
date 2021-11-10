package com.example.sources.domain.repository.evaluationuser;

import com.example.sources.domain.dto.response.EvaluationResponseData;

import java.util.List;

public interface EvaluationUserQuery {
    List<EvaluationResponseData> findAllByUserId(Long userId);
    Boolean existsByEvaluationIdAndUserId(Long evaluationId, Long userId);
}
