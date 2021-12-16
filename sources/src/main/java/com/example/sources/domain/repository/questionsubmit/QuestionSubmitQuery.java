package com.example.sources.domain.repository.questionsubmit;

import com.example.sources.domain.dto.response.QuestionWithSubmittedResponseData;
import com.example.sources.domain.dto.response.SubmittedQuestionResponseData;
import com.example.sources.domain.entity.QuestionSubmit;

import java.util.List;
import java.util.Optional;

public interface QuestionSubmitQuery {
    List<SubmittedQuestionResponseData> findAllByAssignmentIdAndUserId(Long assignmentId, Long userId);
    List<SubmittedQuestionResponseData> findAllByCourseIdAndUserId(Long courseId, Long userId);
    Optional<QuestionSubmit>  findByQuestionIdAndUserId(Long questionId, Long userId);
}
