package com.example.sources.domain.repository.role;

import com.example.sources.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findAllByUserId(Long userId);
}
