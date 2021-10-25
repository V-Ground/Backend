package com.example.sources.domain.repository;

import com.example.sources.domain.entity.Course;
import com.example.sources.domain.entity.User;
import com.example.sources.domain.repository.course.CourseRepository;
import com.example.sources.domain.repository.user.UserRepository;
import com.example.sources.domain.type.OsType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
class CourseRepositoryTest {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("course 생성")
    void create() {
        User teacher = userRepository.save(User.builder()
                .email("test@test.com")
                .username("test")
                .password("asdfasdf")
                .build());

        Course course = Course.builder()
                .os(OsType.UBUNTU_18_04)
                .cpu("1")
                .memory("1")
                .title("모의 해킹 기초")
                .description("모의 해킹 기초에 대해서 학습하실 수 있습니다.")
                .teacher(teacher)
                .build();

        Course savedCourse = courseRepository.save(course);

        assertEquals(1, savedCourse.getId());
        assertEquals("test@test.com", savedCourse.getTeacher().getEmail());
    }
}