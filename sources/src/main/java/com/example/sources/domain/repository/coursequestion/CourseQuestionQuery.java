package com.example.sources.domain.repository.coursequestion;

import com.example.sources.domain.dto.response.AssignmentDetailResponseData;
import com.example.sources.domain.dto.response.AssignmentResponseData;
import com.example.sources.domain.dto.response.QuestionResponseData;

import java.util.List;
import java.util.Optional;

public interface CourseQuestionQuery {
    Optional<AssignmentDetailResponseData> findAssignmentDetailById(Long assignmentId);
    List<QuestionResponseData> findAllByCourseId(Long courseId);
    List<QuestionResponseData> findAllByAssignmentId(Long assignmentId);
}
