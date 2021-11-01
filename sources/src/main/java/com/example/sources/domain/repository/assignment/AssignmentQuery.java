package com.example.sources.domain.repository.assignment;

import com.example.sources.domain.dto.response.AssignmentResponseData;

import java.util.List;

public interface AssignmentQuery {
    List<AssignmentResponseData> findAllByCourseId(Long courseId);
}
