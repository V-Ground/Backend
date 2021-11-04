package com.example.sources.domain.repository.coursequestion;

import com.example.sources.domain.dto.response.AssignmentDetailResponseData;

import java.util.Optional;

public interface CourseQuestionQuery {
    Optional<AssignmentDetailResponseData> findAssignmentDetailById(Long assignmentId);
}
