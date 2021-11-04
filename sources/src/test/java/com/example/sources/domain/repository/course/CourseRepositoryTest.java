package com.example.sources.domain.repository.course;

import com.example.sources.domain.dto.response.AssignmentDetailResponseData;
import com.example.sources.domain.dto.response.AssignmentResponseData;
import com.example.sources.domain.dto.response.CourseResponseData;
import com.example.sources.domain.entity.*;
import com.example.sources.domain.repository.assignment.AssignmentRepository;
import com.example.sources.domain.repository.course.CourseRepository;
import com.example.sources.domain.repository.coursequestion.CourseQuestionRepository;
import com.example.sources.domain.repository.coursestudent.CourseUserRepository;
import com.example.sources.domain.repository.questionsubmit.QuestionSubmitRepository;
import com.example.sources.domain.repository.user.UserRepository;
import com.example.sources.domain.type.OsType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
class CourseRepositoryTest {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseUserRepository courseUserRepository;
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private CourseQuestionRepository courseQuestionRepository;
    @Autowired
    private QuestionSubmitRepository questionSubmitRepository;

    @BeforeEach
    void setUp() {
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

        Assignment assignment = assignmentRepository.save(Assignment.builder()
                .title("공격자 정보 파악하기")
                .description("패킷을 분석하고 공격자의 정보를 파악하라")
                .course(course)
                .build());

        CourseQuestion courseQuestion = courseQuestionRepository.save(CourseQuestion.builder()
                .question("공격자의 IP는?")
                .description("공격자의 IP를 x.x.x.x 형태로 입력하세요")
                .answer("192.168.0.2")
                .assignment(assignment)
                .score(100)
                .build());

        courseQuestionRepository.save(CourseQuestion.builder()
                .question("공격자가 탈취한 정보는?")
                .description("공격자가 탈취한 정보를 3가지 이상 입력하세요")
                .answer("passwd 파일, sshd, bash_history")
                .assignment(assignment)
                .score(100)
                .build());

        questionSubmitRepository.save(QuestionSubmit.builder()
                .answer("192.168.0.2")
                .user(student)
                .question(courseQuestion)
                .build());
    }

    @Test
    @DisplayName("학생이 소속된 모든 course 조회")
    void getCourses() {
        List<CourseResponseData> courses = courseUserRepository.findAllByUserId(13L);
        assertEquals(0, courses.size());
    }

    @Test
    @DisplayName("학생이 특정 courseId 에 소속되어있는지 확인")
    void existsCourse() {
        boolean isCourseMember = courseUserRepository.existsByCourseIdAndUserId(1L, 10L);
        assertTrue(isCourseMember);
    }

    @Test
    @DisplayName("특정 course 에 소속된 모든 과제 조회")
    void getAllAssignmentByCourse() {
        List<AssignmentResponseData> assignments = assignmentRepository.findAllByCourseId(1L);

        for (AssignmentResponseData assignment : assignments) {
            System.out.println("assignment = " + assignment);
        }
        assertEquals(1, assignments.size());
    }

    @Test
    @DisplayName("특정 assignment 에 소속된 모든 주관식 문제 조회")
    void getAllQuestionByAssignment() {
        AssignmentDetailResponseData questions = courseQuestionRepository.findAssignmentDetailById(1L).get();
        System.out.println("questions = " + questions);
    }
}