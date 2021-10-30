package com.example.sources.domain.repository.quizsubmit;

import com.example.sources.domain.entity.QuizSubmit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizSubmitRepository extends JpaRepository<QuizSubmit, Long>, QuizSubmitQuery {
}
