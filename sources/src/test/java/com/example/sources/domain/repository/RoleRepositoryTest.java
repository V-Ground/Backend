package com.example.sources.domain.repository;

import com.example.sources.domain.entity.Role;
import com.example.sources.domain.repository.role.RoleRepository;
import com.example.sources.domain.type.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("userId 로 관련된 Role 찾기")
    void findAllByUserId() {
        Role teacher = Role.builder()
                .roleType(RoleType.TEACHER)
                .userId(1L)
                .build();

        Role student = Role.builder()
                .roleType(RoleType.STUDENT)
                .userId(1L)
                .build();

        roleRepository.save(teacher);
        roleRepository.save(student);

        List<Role> roles = roleRepository.findAllByUserId(1L);

        assertEquals(2, roles.size());
        assertEquals(RoleType.TEACHER, roles.get(0).getRoleType());
        assertEquals(RoleType.STUDENT, roles.get(1).getRoleType());
    }
}