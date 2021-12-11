package com.example.sources.controller;

import com.example.sources.domain.dto.request.CreateAssignmentReqData;
import com.example.sources.domain.dto.request.CreateCourseReqData;
import com.example.sources.domain.dto.request.CreateQuestionRequestData;
import com.example.sources.domain.dto.response.*;
import com.example.sources.domain.entity.Role;
import com.example.sources.domain.type.RoleType;
import com.example.sources.service.AssignmentService;
import com.example.sources.service.AuthenticationService;
import com.example.sources.service.CourseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseController.class)
class CourseControllerTest {

    private static final Cookie[] TEACHER_COOKIES = {new Cookie("accessToken", "a.b.c")};
    private static final Cookie[] STUDENT_COOKIES = {new Cookie("accessToken", "d.e.f")};
    private static final String TEACHER_TOKEN = "a.b.c";
    private static final String STUDENT_TOKEN = "d.e.f";
    private static final Long TEACHER_ID = 1L;
    private static final Long TEACHER_TOKEN_ID = 1L;
    private static final Long STUDENT_ID = 2L;
    private static final Long STUDENT_TOKEN_ID = 2L;
    private static final Long COURSE_ID = 1L;
    private static final Long ASSIGNMENT_ID = 1L;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private CourseService courseService;
    @MockBean
    private AssignmentService assignmentService;

    @BeforeEach
    void setUp() {

        given(authenticationService.getTokenFromCookies(TEACHER_COOKIES))
                .willReturn(TEACHER_TOKEN);
        given(authenticationService.parseToken(TEACHER_TOKEN))
                .willReturn(TEACHER_ID);

        given(authenticationService.getTokenFromCookies(STUDENT_COOKIES))
                .willReturn(STUDENT_TOKEN);
        given(authenticationService.parseToken(STUDENT_TOKEN))
                .willReturn(STUDENT_ID);

        given(authenticationService.getRoles(TEACHER_ID))
                .willReturn(List.of(new Role(TEACHER_ID, RoleType.TEACHER), new Role(TEACHER_ID, RoleType.STUDENT)));

        given(authenticationService.getRoles(STUDENT_ID))
                .willReturn(List.of(new Role(STUDENT_ID, RoleType.STUDENT)));

        given(courseService.addCourse(any(CreateCourseReqData.class)))
                .willReturn(new CreateCourseResponseData());

        given(assignmentService.addAssignment(eq(COURSE_ID), any(), eq(TEACHER_TOKEN_ID)))
                .willReturn(new AssignmentResponseData());

        given(assignmentService.getAssignments(COURSE_ID, STUDENT_TOKEN_ID))
                .willReturn(List.of(new AssignmentResponseData()));

        given(assignmentService.addQuestion(eq(COURSE_ID), eq(ASSIGNMENT_ID), any(), eq(TEACHER_TOKEN_ID)))
                .willReturn(new CreateQuizResponseData());

        given(assignmentService.getAssignmentDetail(COURSE_ID, ASSIGNMENT_ID, STUDENT_ID, STUDENT_TOKEN_ID))
                .willReturn(new QuestionWithSubmittedResponseData());
    }

    @Test
    @DisplayName("클래스 생성 성공")
    void addCourse_success() throws Exception {
        CreateCourseReqData request = new CreateCourseReqData();

        mockMvc.perform(post("/api/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .cookie(TEACHER_COOKIES))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("클래스 과제 생성")
    void addAssignment_success() throws Exception {
        CreateAssignmentReqData request = new CreateAssignmentReqData();

        mockMvc.perform(post("/api/v1/courses/{courseId}/assignments", COURSE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .cookie(TEACHER_COOKIES))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("모든 과제 조회")
    void getAssignments_success() throws Exception {
        mockMvc.perform(get("/api/v1/courses/{courseId}/assignments", COURSE_ID)
                        .cookie(STUDENT_COOKIES))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("과제에 문제 생성")
    void addAssignmentQuestion() throws Exception {
        List<CreateQuestionRequestData> request = List.of(new CreateQuestionRequestData());

        mockMvc.perform(post("/api/v1/courses/{courseId}/assignments/{assignmentId}", COURSE_ID, ASSIGNMENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .cookie(TEACHER_COOKIES))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("과제 세부 내용 조회")
    void getAssignment() throws Exception {
        mockMvc.perform(get("/api/v1/courses/{courseId}/assignments/{assignmentId}/users/{userId}",
                            COURSE_ID,
                            ASSIGNMENT_ID,
                            STUDENT_ID)
                        .cookie(STUDENT_COOKIES))
                .andDo(print())
                .andExpect(status().isOk());
    }

}