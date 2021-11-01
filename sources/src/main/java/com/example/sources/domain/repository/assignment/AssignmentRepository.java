package com.example.sources.domain.repository.assignment;

import com.example.sources.domain.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository extends JpaRepository<Assignment, Long>, AssignmentQuery {
}
