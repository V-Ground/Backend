package com.example.sources.domain.repository;

import com.example.sources.domain.entity.SubjectiveAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectiveAnswerRepository extends JpaRepository<SubjectiveAnswer, Long> {
}
