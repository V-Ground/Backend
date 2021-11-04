package com.example.sources.domain.repository.course;

import com.example.sources.domain.dto.response.CourseResponseData;
import com.example.sources.domain.entity.Course;
import com.example.sources.domain.entity.CourseUser;
import com.example.sources.domain.entity.User;
import com.example.sources.domain.repository.course.CourseRepository;
import com.example.sources.domain.repository.coursestudent.CourseUserRepository;
import com.example.sources.domain.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    void setUp() {

    }


}