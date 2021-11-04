package com.example.sources.domain.repository.assignment;

import com.example.sources.domain.dto.response.AssignmentDetailResponseData;
import com.example.sources.domain.dto.response.AssignmentResponseData;

import java.util.List;
import java.util.Optional;

public interface AssignmentQuery {
    List<AssignmentResponseData> findAllByCourseId(Long courseId);
}
