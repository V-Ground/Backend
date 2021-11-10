package com.example.sources.controller;

import com.example.sources.domain.dto.request.CreateEvaluationRequestData;
import com.example.sources.domain.dto.request.CreateQuizRequestData;
import com.example.sources.domain.dto.response.CreateEvaluationResponseData;
import com.example.sources.domain.dto.response.CreateQuizResponseData;
import com.example.sources.domain.dto.response.QuizResponseData;
import com.example.sources.domain.entity.Role;
import com.example.sources.domain.type.RoleType;
import com.example.sources.service.AuthenticationService;
import com.example.sources.service.EvaluationService;
import com.example.sources.service.QuizService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EvaluationController.class)
class EvaluationControllerTest {

    private static final Cookie[] TEACHER_COOKIES = {new Cookie("accessToken", "a.b.c")};
    private static final Cookie[] STUDENT_COOKIES = {new Cookie("accessToken", "d.e.f")};
    private static final String TEACHER_TOKEN = "a.b.c";
    private static final String STUDENT_TOKEN = "d.e.f";
    private static final Long TEACHER_ID = 1L;
    private static final Long TEACHER_TOKEN_ID = 1L;
    private static final Long STUDENT_ID = 2L;
    private static final Long STUDENT_TOKEN_ID = 2L;
    private static final Long EVALUATION_ID = 1L;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private EvaluationService evaluationService;
    @MockBean
    private QuizService quizService;

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

        given(evaluationService.addEvaluation(any()))
                .willReturn(new CreateEvaluationResponseData());

        given(quizService.addEvaluationQuiz(eq(EVALUATION_ID), any()))
                .willReturn(new CreateQuizResponseData());

        given(quizService.getAllQuizzes(EVALUATION_ID))
                .willReturn(List.of(new QuizResponseData()));
    }

    @Test
    @DisplayName("테스트 생성")
    void addEvaluation() throws Exception {
        CreateEvaluationRequestData request = new CreateEvaluationRequestData();

        mockMvc.perform(post("/api/v1/evaluations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .cookie(TEACHER_COOKIES))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("테스트 퀴즈 추가")
    void addEvaluationQuiz() throws Exception {
        List<CreateQuizRequestData> request = List.of(new CreateQuizRequestData());

        mockMvc.perform(post("/api/v1/evaluations/{evaluationId}/quizzes", EVALUATION_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .cookie(TEACHER_COOKIES))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("테스트 문제 모두 조회")
    void  getAllEvaluationQuiz() throws Exception {
        mockMvc.perform(get("/api/v1/evaluations/{evaluationId}/quizzes", EVALUATION_ID)
                        .cookie(STUDENT_COOKIES))
                .andDo(print())
                .andExpect(status().isOk());
    }
}