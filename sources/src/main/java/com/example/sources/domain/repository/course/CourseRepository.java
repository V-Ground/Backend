package com.example.sources.domain.repository.course;

import com.example.sources.domain.dto.response.CourseResponseData;
import com.example.sources.domain.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long>, CourseQuery {
}
