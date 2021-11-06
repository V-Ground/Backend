package com.example.sources.domain.repository.evaluation;

import com.example.sources.domain.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
}
