package com.example.sources.controller;

import com.example.sources.domain.dto.request.CreateCourseRequestData;
import com.example.sources.domain.dto.response.CreateCourseResponseData;
import com.example.sources.domain.entity.Role;
import com.example.sources.domain.type.RoleType;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseController.class)
class CourseControllerTest {

    private final String TEACHER_TOKEN = "a.b.c";
    private final Long TEACHER_ID = 1L;
    private final Long STUDENT_ID = 2L;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private CourseService courseService;

    @BeforeEach
    void setUp() {

        given(authenticationService.parseToken(TEACHER_TOKEN))
                .willReturn(TEACHER_ID);
        given(authenticationService.getTokenFromCookies(any()))
                .willReturn(TEACHER_TOKEN);

        given(authenticationService.getRoles(TEACHER_ID))
                .willReturn(List.of(new Role(TEACHER_ID, RoleType.TEACHER), new Role(TEACHER_ID, RoleType.STUDENT)));

        given(courseService.addCourse(any(CreateCourseRequestData.class)))
                .willReturn(new CreateCourseResponseData());
    }

    @Test
    @DisplayName("클래스 생성 성공")
    void createCourse_success() throws Exception {
        CreateCourseRequestData request = new CreateCourseRequestData();
        Cookie cookie = new Cookie("accessToken", TEACHER_TOKEN);

        mockMvc.perform(post("/api/v1/course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .cookie(cookie))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}