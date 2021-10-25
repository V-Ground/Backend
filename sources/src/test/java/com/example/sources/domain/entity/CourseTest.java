package com.example.sources.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

    @Test
    @DisplayName("course 생성")
    void create() {
        Course course = new Course();
        User user = new User();

        assertNull(course.getTeacher());

        course.create(user);

        assertNotNull(course.getTeacher());
    }

}