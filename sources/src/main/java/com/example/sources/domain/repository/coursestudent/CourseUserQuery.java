package com.example.sources.domain.repository.coursestudent;

import com.example.sources.domain.dto.response.CourseResponseData;
import com.example.sources.domain.entity.Course;

import java.util.List;

public interface CourseUserQuery {
    List<CourseResponseData> findAllByUserId(Long userId);
    Boolean existsByCourseIdAndUserId(Long courseId, Long userId);
}
