package com.example.sources.domain.repository.questionsubmit;

import com.example.sources.domain.dto.response.SubmittedQuestionResponseData;

import java.util.List;

public interface QuestionSubmitQuery {
    List<SubmittedQuestionResponseData> findAllByAssignmentIdAndUserId(Long assignmentId, Long userId);
}
