package com.example.sources.domain.repository;

import com.example.sources.domain.dto.response.*;
import com.example.sources.domain.entity.*;
import com.example.sources.domain.repository.assignment.AssignmentRepository;
import com.example.sources.domain.repository.course.CourseRepository;
import com.example.sources.domain.repository.coursequestion.CourseQuestionRepository;
import com.example.sources.domain.repository.courseuser.CourseUserRepository;
import com.example.sources.domain.repository.interaction.InteractionRepository;
import com.example.sources.domain.repository.interactionsubmit.InteractionSubmitRepository;
import com.example.sources.domain.repository.questionsubmit.QuestionSubmitRepository;
import com.example.sources.domain.repository.user.UserRepository;
import com.example.sources.domain.type.InteractionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
class CourseRelatedRepositoryTest {


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
    @Autowired
    private InteractionRepository interactionRepository;
    @Autowired
    private InteractionSubmitRepository interactionSubmitRepository;

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
                .title("??????????????? ??????")
                .description("?????????????????? ????????? ????????? ???????????????")
                .visibility(true)
                .teacher(teacher)
                .build());

        Course course2 = courseRepository.save(Course.builder()
                .title("???????????? ??????")
                .description("??????????????? ????????? ????????? ???????????????")
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
                .title("????????? ?????? ????????????")
                .description("????????? ???????????? ???????????? ????????? ????????????")
                .course(course)
                .build());

        CourseQuestion courseQuestion = courseQuestionRepository.save(CourseQuestion.builder()
                .question("???????????? IP????")
                .description("???????????? IP??? x.x.x.x ????????? ???????????????")
                .answer("192.168.0.2")
                .assignment(assignment)
                .score(100)
                .build());

        courseQuestionRepository.save(CourseQuestion.builder()
                .question("???????????? ????????? ??????????")
                .description("???????????? ????????? ????????? 3?????? ?????? ???????????????")
                .answer("passwd ??????, sshd, bash_history")
                .assignment(assignment)
                .score(100)
                .build());

        questionSubmitRepository.save(QuestionSubmit.builder()
                .answer("192.168.0.2")
                .user(student)
                .question(courseQuestion)
                .build());

        Interaction interaction = interactionRepository.save(Interaction.builder()
                .title("?????? ???????????? ????????? ??????????????????????")
                .interactionType(InteractionType.OX)
                .createdAt(LocalDateTime.now())
                .course(course)
                .build());

        interactionSubmitRepository.save(InteractionSubmit.builder()
                .yesNo(true)
                .user(student)
                .interaction(interaction)
                .build());
    }

    @Test
    @DisplayName("????????? ????????? ?????? course ??????")
    void getCourses() {
        List<CourseResponseData> courses = courseUserRepository.findAllByUserId(13L);
        assertEquals(0, courses.size());
    }

    @Test
    @DisplayName("????????? ?????? courseId ??? ????????????????????? ??????")
    void existsCourse() {
        boolean isCourseMember = courseUserRepository.existsByCourseIdAndUserId(1L, 10L);
        assertTrue(isCourseMember);
    }

    @Test
    @DisplayName("?????? course ??? ????????? ?????? ?????? ??????")
    void getAllAssignmentByCourse() {
        List<AssignmentResponseData> assignments = assignmentRepository.findAllByCourseId(1L);

        for (AssignmentResponseData assignment : assignments) {
            System.out.println("assignment = " + assignment);
        }
        assertEquals(1, assignments.size());
    }

    @Test
    @DisplayName("?????? assignment ??? ????????? ?????? ????????? ?????? ??????")
    void getAllQuestionByAssignment() {
        AssignmentDetailResponseData questions = courseQuestionRepository.findAssignmentDetailById(1L).get();

        assertAll(
                () -> assertNotNull(questions.getAssignment()),
                () -> assertEquals(2, questions.getQuestions().size())
        );
    }

    @Test
    @DisplayName("????????? ????????? ????????? ?????? ??????")
    void getAllSubmittedQuestion() {
        List<SubmittedQuestionResponseData> submittedQuestions = questionSubmitRepository
                .findAllByAssignmentIdAndUserId(1L, 10L);

        assertTrue(0 != submittedQuestions.size());
    }

    @Test
    @DisplayName("?????? ???????????? ??????")
    void getAllInteraction() {
        List<InteractionResponseData> allInteractions = interactionRepository.findAllByCourseId(3L);

        assertTrue(0 != allInteractions.size());
    }

    @Test
    @DisplayName("?????? ???????????? ?????? ??? ??????")
    void getAllInteractionSubmit() {
        List<InteractionSubmitResponseData> allSubmit = interactionSubmitRepository.findAllByInteractionId(1L);

        assertAll(
                () -> assertEquals(1, allSubmit.size()),
                () -> assertEquals(true, allSubmit.get(0).getYesNo()),
                () -> assertEquals("jwi", allSubmit.get(0).getStudentName())
        );
    }

    @Test
    @DisplayName("???????????? ????????? ?????? ?????? ??????")
    void getCourseUserByStudentId() {
        CourseUser courseUser = courseUserRepository.findByUserId(10L).get();

        assertNotNull(courseUser);
    }

    @Test
    @DisplayName("???????????? ????????? ?????? ?????? ??????")
    void getCourseUserByCourseId() {
        List<CourseUser> allByCourseId = courseUserRepository.findAllByCourseId(2L);

        for (CourseUser s : allByCourseId) {
            System.out.println("s = " + s.getUser().getUsername());
        }
    }
}