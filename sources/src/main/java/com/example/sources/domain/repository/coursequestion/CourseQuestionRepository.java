package com.example.sources.domain.repository.coursequestion;

import com.example.sources.domain.entity.CourseQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseQuestionRepository extends JpaRepository<CourseQuestion, Long>, CourseQuestionQuery {
}
