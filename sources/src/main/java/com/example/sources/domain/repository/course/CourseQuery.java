package com.example.sources.domain.repository.course;

import com.example.sources.domain.dto.response.CourseResponseData;

import java.util.List;

public interface CourseQuery {
    List<CourseResponseData> findAllByUserId(Long targetUserId);
}
