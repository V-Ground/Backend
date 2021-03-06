package com.example.sources.service;

import com.example.sources.domain.dto.request.CreateAssignmentReqData;
import com.example.sources.domain.dto.request.CreateQuestionRequestData;
import com.example.sources.domain.dto.request.SolveQuestionRequestData;
import com.example.sources.domain.dto.response.*;
import com.example.sources.domain.entity.*;
import com.example.sources.domain.repository.assignment.AssignmentRepository;
import com.example.sources.domain.repository.course.CourseRepository;
import com.example.sources.domain.repository.coursequestion.CourseQuestionRepository;
import com.example.sources.domain.repository.courseuser.CourseUserRepository;
import com.example.sources.domain.repository.questionsubmit.QuestionSubmitRepository;
import com.example.sources.domain.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class AssignmentServiceTest {

    private static final Long COURSE_ID = 1L;
    private static final Long TEACHER_ID = 1L;
    private static final Long STUDENT_ID = 2L;
    private static final Long ASSIGNMENT_ID = 1L;
    private static final Long COURSE_QUESTION_ID = 1L;
    private static final Long VALID_TOKEN_TEACHER_ID = 1L;
    private static final Long VALID_TOKEN_STUDENT_ID = 2L;

    private final AssignmentRepository assignmentRepository = mock(AssignmentRepository.class);
    private final CourseRepository courseRepository = mock(CourseRepository.class);
    private final CourseUserRepository courseUserRepository = mock(CourseUserRepository.class);
    private final CourseQuestionRepository courseQuestionRepository = mock(CourseQuestionRepository.class);
    private final QuestionSubmitRepository questionSubmitRepository = mock(QuestionSubmitRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);

    private AssignmentService assignmentService;

    @BeforeEach
    void setUp() {
        assignmentService = new AssignmentService(assignmentRepository,
                courseRepository,
                courseUserRepository,
                courseQuestionRepository,
                questionSubmitRepository,
                userRepository,
                new ModelMapper());

        User teacher = User.builder().id(TEACHER_ID).build();
        Course course = Course.builder().id(COURSE_ID).teacher(teacher).build();
        Assignment assignment = Assignment.builder().title("??????1").description("?????? ??????").course(course).build();
        List<QuestionResponseData> questions = List.of(new QuestionResponseData(), new QuestionResponseData());

        given(userRepository.findById(STUDENT_ID))
                .willReturn(Optional.of(new User()));

        given(courseRepository.findById(COURSE_ID)) // ?????? ??????, ?????? ?????? ??????, ????????? ??????, ????????? ?????? ??????
                .willReturn(Optional.of(course));

        given(assignmentRepository.save(any())) // ?????? ??????
                .willReturn(Assignment.builder().id(ASSIGNMENT_ID).title("title").description("test").build());

        given(courseUserRepository.existsByCourseIdAndUserId(COURSE_ID, TEACHER_ID))
                .willReturn(true); // ?????? ?????? ??????, ?????? ?????? ?????? ??????(?????????)

        given(courseUserRepository.existsByCourseIdAndUserId(COURSE_ID, STUDENT_ID))
                .willReturn(true); // ?????? ?????? ??????, ?????? ?????? ?????? ??????(?????????)

        given(assignmentRepository.findAllByCourseId(COURSE_ID)) // ?????? ?????? ??????
                .willReturn(List.of(new AssignmentResponseData()));

        given(assignmentRepository.findById(ASSIGNMENT_ID)) // ?????? ??????
                .willReturn(Optional.of(assignment));

        given(courseQuestionRepository.save(any())) // ?????? ??????
                .willReturn(new CourseQuestion());

        given(courseQuestionRepository.findAssignmentDetailById(ASSIGNMENT_ID)) // ?????? ?????? ?????? ??????(?????????)
                .willReturn(Optional.of(AssignmentDetailResponseData.builder()
                        .assignment(new AssignmentResponseData()).questions(questions).build()));

        given(courseQuestionRepository.findById(COURSE_QUESTION_ID))
                .willReturn(Optional.of(new CourseQuestion()));

        given(questionSubmitRepository.save(any()))
                .willReturn(new QuestionSubmit());

        given(questionSubmitRepository.findAllByAssignmentIdAndUserId(ASSIGNMENT_ID, STUDENT_ID))
                .willReturn(List.of(new SubmittedQuestionResponseData()));
    }

    @Test
    @DisplayName("?????? ?????? - ??????")
    void addAssignment_success() {
        CreateAssignmentReqData request = CreateAssignmentReqData.builder()
                .title("title")
                .description("test")
                .build();

        AssignmentResponseData response = assignmentService.addAssignment(COURSE_ID, request, VALID_TOKEN_TEACHER_ID);

        assertEquals(1, response.getAssignmentId());
    }

    @Test
    @DisplayName("course ??? ????????? ?????? ?????? ?????? - ??????")
    void getAssignments_success() {
        List<AssignmentResponseData> assignments = assignmentService.getAssignments(STUDENT_ID, VALID_TOKEN_STUDENT_ID);

        assertEquals(1, assignments.size());
    }

    @Test
    @DisplayName("?????? ????????? ?????? - ??????")
    void addQuestions_success() {
        List<CreateQuestionRequestData> request = List.of(new CreateQuestionRequestData(), new CreateQuestionRequestData());

        CreateQuizResponseData createQuizResponseData = assignmentService
                .addQuestion(COURSE_ID, ASSIGNMENT_ID, request, VALID_TOKEN_TEACHER_ID);

        assertEquals(2, createQuizResponseData.getCreatedQuizNumber());
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????? - ??????")
    void getAssignmentDetail_success() {
        QuestionWithSubmittedResponseData detail = assignmentService
                .getAssignmentDetail(COURSE_ID, ASSIGNMENT_ID, STUDENT_ID, VALID_TOKEN_STUDENT_ID);

        assertAll(
                () -> assertNotNull(detail.getQuestionDetail()),
                () -> assertNotNull(detail.getSubmittedAnswerDetail())
        );
    }

    @Test
    @DisplayName("????????? ?????? ?????? - ??????")
    void solveQuestion() {
        SolveQuestionRequestData request = new SolveQuestionRequestData("????????? ????????? ??????");

        SolveQuestionRequestData response = assignmentService
                .solveQuestion(STUDENT_ID, COURSE_ID, ASSIGNMENT_ID, COURSE_QUESTION_ID, request, VALID_TOKEN_STUDENT_ID);

        assertNotNull(response);
    }

    @Test
    @DisplayName("????????? ????????? ?????? ????????????")
    void getSubmittedQuestions() {
        List<SubmittedQuestionResponseData> answers = assignmentService
                .getSubmittedQuestions(TEACHER_ID, COURSE_ID, ASSIGNMENT_ID, STUDENT_ID, VALID_TOKEN_TEACHER_ID);

        assertEquals(1, answers.size());
    }
}