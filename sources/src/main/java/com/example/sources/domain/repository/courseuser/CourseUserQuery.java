package com.example.sources.domain.repository.courseuser;

import com.example.sources.domain.dto.response.CourseResponseData;

import java.util.List;

public interface CourseUserQuery {
    List<CourseResponseData> findAllByUserId(Long userId);
    Boolean existsByCourseIdAndUserId(Long courseId, Long userId);
}
