package com.example.sources.domain.repository.questionsubmit;

import com.example.sources.domain.entity.QuestionSubmit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionSubmitRepository extends JpaRepository<QuestionSubmit, Long>, QuestionSubmitQuery {
}
