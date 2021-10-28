package com.example.sources.domain.repository.evaluationuser;

import com.example.sources.domain.entity.EvaluationUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationUserRepository extends JpaRepository<EvaluationUser, Long>, EvaluationUserQuery {
}
