package com.example.sources.domain.repository;

import com.example.sources.domain.dto.response.CourseResponseData;
import com.example.sources.domain.entity.Course;
import com.example.sources.domain.entity.CourseUser;
import com.example.sources.domain.entity.User;
import com.example.sources.domain.repository.course.CourseRepository;
import com.example.sources.domain.repository.coursestudent.CourseUserRepository;
import com.example.sources.domain.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
class CourseUserRepositoryTest {
    @Autowired
    private CourseUserRepository courseUserRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Test
    @DisplayName("학생이 소속된 모든 course 조회")
    void getCourses() {
        User teacher = userRepository.save(User.builder()
                .username("kkt")
                .email("kkt@gmail.com")
                .build());
        User student = userRepository.save(User.builder()
                .username("jwi")
                .email("jwi@gmail.com")
                .build());

        Course course = courseRepository.save(Course.builder()
                .title("와이어샤크 기초")
                .description("와이어샤크의 기초에 대해서 학습합니다")
                .visibility(true)
                .teacher(teacher)
                .build());

        Course course2 = courseRepository.save(Course.builder()
                .title("클라우드 기초")
                .description("클라우스의 기초에 대해서 학습합니다")
                .visibility(true)
                .teacher(teacher)
                .build());

        courseUserRepository.save(CourseUser.builder()
                .user(student)
                .course(course)
                .build());

        courseUserRepository.save(CourseUser.builder()
                .user(student)
                .course(course2)
                .build());

        List<CourseResponseData> allByUserId = courseUserRepository.findAllByUserId(student.getId());

        for (CourseResponseData courseResponseData : allByUserId) {
            System.out.println("courseResponseData = " + courseResponseData);
        }
    }
}