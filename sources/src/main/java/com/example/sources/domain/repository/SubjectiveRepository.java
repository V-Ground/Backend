package com.example.sources.domain.repository;

import com.example.sources.domain.entity.Subjective;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectiveRepository extends JpaRepository<Subjective, Long> {
}
