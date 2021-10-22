package com.example.sources.domain.repository;

import com.example.sources.domain.entity.User;
import com.example.sources.domain.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .email("test@test.com")
                .username("테스트")
                .password("123")
                .build();
        userRepository.save(user);
    }

    @Test
    @DisplayName("email 로 회원 검색")
    void findByEmail() {
        User user = userRepository.findByEmail("test@test.com").get();

        assertEquals("테스트", user.getUsername());
    }

}