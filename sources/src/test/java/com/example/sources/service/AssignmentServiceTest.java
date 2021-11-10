package com.example.sources.service;

import com.example.sources.domain.dto.request.CreateAssignmentRequestData;
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

import java.util.ArrayList;
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
        Assignment assignment = Assignment.builder().title("과제1").description("과제 설명").course(course).build();
        List<QuestionResponseData> questions = List.of(new QuestionResponseData(), new QuestionResponseData());

        given(userRepository.findById(STUDENT_ID))
                .willReturn(Optional.of(new User()));

        given(courseRepository.findById(COURSE_ID)) // 과제 생성, 모든 과제 조회, 주관식 생성, 주관식 정답 제출
                .willReturn(Optional.of(course));

        given(assignmentRepository.save(any())) // 과제 생성
                .willReturn(Assignment.builder().id(ASSIGNMENT_ID).title("title").description("test").build());

        given(courseUserRepository.existsByCourseIdAndUserId(COURSE_ID, TEACHER_ID))
                .willReturn(true); // 모든 과제 조회, 과제 세부 내용 조회(주관식)

        given(courseUserRepository.existsByCourseIdAndUserId(COURSE_ID, STUDENT_ID))
                .willReturn(true); // 모든 과제 조회, 과제 세부 내용 조회(주관식)

        given(assignmentRepository.findAllByCourseId(COURSE_ID)) // 모든 과제 조회
                .willReturn(List.of(new AssignmentResponseData()));

        given(assignmentRepository.findById(ASSIGNMENT_ID)) // 과제 생성
                .willReturn(Optional.of(assignment));

        given(courseQuestionRepository.save(any())) // 과제 생성
                .willReturn(new CourseQuestion());

        given(courseQuestionRepository.findAssignmentDetailById(ASSIGNMENT_ID)) // 과제 세부 내용 조회(주관식)
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
    @DisplayName("과제 생성 - 성공")
    void addAssignment_success() {
        CreateAssignmentRequestData request = CreateAssignmentRequestData.builder()
                .title("title")
                .description("test")
                .build();

        AssignmentResponseData response = assignmentService.addAssignment(COURSE_ID, request, VALID_TOKEN_TEACHER_ID);

        assertEquals(1, response.getAssignmentId());
    }

    @Test
    @DisplayName("course 에 포함된 모든 과제 조회 - 성공")
    void getAssignments_success() {
        List<AssignmentResponseData> assignments = assignmentService.getAssignments(STUDENT_ID, VALID_TOKEN_STUDENT_ID);

        assertEquals(1, assignments.size());
    }

    @Test
    @DisplayName("과제 주관식 생성 - 성공")
    void addQuestions_success() {
        List<CreateQuestionRequestData> request = List.of(new CreateQuestionRequestData(), new CreateQuestionRequestData());

        CreateQuizResponseData createQuizResponseData = assignmentService
                .addQuestion(COURSE_ID, ASSIGNMENT_ID, request, VALID_TOKEN_TEACHER_ID);

        assertEquals(2, createQuizResponseData.getCreatedQuizNumber());
    }

    @Test
    @DisplayName("과제 세부 내용 조회 - 성공")
    void getAssignmentDetail_success() {
        QuestionWithSubmittedResponseData detail = assignmentService
                .getAssignmentDetail(COURSE_ID, ASSIGNMENT_ID, STUDENT_ID, VALID_TOKEN_STUDENT_ID);

        assertAll(
                () -> assertNotNull(detail.getQuestionDetail()),
                () -> assertNotNull(detail.getSubmittedAnswerDetail())
        );
    }

    @Test
    @DisplayName("주관식 정답 제출 - 성공")
    void solveQuestion() {
        SolveQuestionRequestData request = new SolveQuestionRequestData("학생이 제출한 정답");

        SolveQuestionRequestData response = assignmentService
                .solveQuestion(STUDENT_ID, COURSE_ID, ASSIGNMENT_ID, COURSE_QUESTION_ID, request, VALID_TOKEN_STUDENT_ID);

        assertNotNull(response);
    }

    @Test
    @DisplayName("학생이 제출한 정답 확인하기")
    void getSubmittedQuestions() {
        List<SubmittedQuestionResponseData> answers = assignmentService
                .getSubmittedQuestions(TEACHER_ID, COURSE_ID, ASSIGNMENT_ID, STUDENT_ID, VALID_TOKEN_TEACHER_ID);

        assertEquals(1, answers.size());
    }
}