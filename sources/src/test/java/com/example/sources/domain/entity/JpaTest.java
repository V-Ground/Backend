package com.example.sources.domain.entity;

import com.example.sources.domain.repository.*;
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
class JpaTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private AssignmentSubmitRepository assignmentSubmitRepository;
    @Autowired
    private SubjectiveRepository subjectiveRepository;
    @Autowired
    private SubjectiveAnswerRepository subjectiveAnswerRepository;

    @BeforeEach
    void setUp() {
        User user = userRepository.save(User.builder()
                .username("장원익")
                .build());

        Course course = courseRepository.save(Course.builder()
                .title("와이어샤크 기초")
                .build());

        Assignment assignment = assignmentRepository.save(Assignment.builder()
                .title("와이어샤크 패킷 분석하기")
                .description("와이어샤크 패킷을 분석하고 아래 문제를 해결하세요")
                .course(course)
                .build());

        // 문제 생성
        Subjective save = subjectiveRepository.save(Subjective.builder().question("문제 1 : 공격자의 IP 는?").assignment(assignment).score(100).build());
        assignment.addSubjectives(save);
        assignmentRepository.save(assignment);
        subjectiveRepository.save(Subjective.builder().question("문제 2 : 공격자가 공격하는 port 는?").assignment(assignment).score(100).build());
        subjectiveRepository.save(Subjective.builder().question("문제 1 : 희생자의 IP 는?").assignment(assignment).score(100).build());
    }

    @Test
    @DisplayName("문제 해결")
    void clear() {
        Assignment assignment = assignmentRepository.findById(1L).get();

        System.out.println("assignment = " + assignment.getTitle());
        System.out.println("assignment = " + assignment.getDescription());
        System.out.println("assignment subjectives = " + assignment.getSubjectives());
    }

}