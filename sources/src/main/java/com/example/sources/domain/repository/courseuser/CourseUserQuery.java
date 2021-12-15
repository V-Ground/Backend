package com.example.sources.domain.repository.courseuser;

import com.example.sources.domain.dto.response.CourseResponseData;
import com.example.sources.domain.dto.response.ParticipantResponseData;
import com.example.sources.domain.entity.CourseUser;

import java.util.List;
import java.util.Optional;

public interface CourseUserQuery {
    List<CourseResponseData> findAllByUserId(Long userId);
    Boolean existsByCourseIdAndUserId(Long courseId, Long userId);
    List<ParticipantResponseData> findAllByCourseId(Long courseId);
    Optional<CourseUser> findByUserId(Long userId);
}
