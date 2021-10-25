package com.example.sources.service;

import com.example.sources.domain.dto.request.LoginRequestData;
import com.example.sources.domain.dto.response.LoginResponseData;
import com.example.sources.domain.entity.User;
import com.example.sources.domain.repository.role.RoleRepository;
import com.example.sources.domain.repository.user.UserRepository;
import com.example.sources.exception.LoginFailedException;
import com.example.sources.util.CookieUtil;
import com.example.sources.util.TokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class AuthenticationServiceTest {

    private static final String SECRET = "66555555444444333333222222111111";
    private static final String ENCODED_PASSWORD = "$2a$10$zSnzZDu5Jpyqch0zez9soekcecOTmgT8MFFzG.Sd7vClwexE.syd2";

    private final UserRepository userRepository = mock(UserRepository.class);
    private final RoleRepository roleRepository = mock(RoleRepository.class);

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationService(
                userRepository,
                roleRepository,
                new ModelMapper(),
                new TokenUtil(SECRET),
                new CookieUtil(),
                new BCryptPasswordEncoder());

        // 로그인
        given(userRepository.findByEmail("test@test.com"))
                .willReturn(Optional.of(new User(1L, "test@test.com", "test", ENCODED_PASSWORD)));
    }

    @Test
    @DisplayName("로그인 성공")
    void login_success() {
        LoginResponseData request = authenticationService.login(
                new LoginRequestData("test@test.com", "password"),
                new MockHttpServletResponse());

        assertEquals("test@test.com", request.getEmail());
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void login_fail_invalid_password() {
        LoginFailedException exception = assertThrows(LoginFailedException.class,
                () -> authenticationService.login(
                        new LoginRequestData("test@test.com", "invalid_password"),
                        new MockHttpServletResponse()));

        assertNotNull(exception.getMessage());
    }

    @Test
    @DisplayName("adsf")
    void asdf() {
        // authenticationService.test();
    }

}