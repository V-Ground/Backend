package com.example.sources.domain.repository;

import com.example.sources.domain.entity.User;
import com.example.sources.domain.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("회원 저장")
    void save() {
        User user = User.builder()
                .email("test@test.com")
                .name("테슽")
                .password("123")
                .build();
        User savedUser = userRepository.save(user);

        assertNotNull(savedUser);
    }

}