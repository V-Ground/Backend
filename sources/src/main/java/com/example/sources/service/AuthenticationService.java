package com.example.sources.service;

import com.example.sources.domain.dto.request.LoginRequestData;
import com.example.sources.domain.dto.response.LoginResponseData;
import com.example.sources.domain.entity.Role;
import com.example.sources.domain.entity.User;
import com.example.sources.domain.repository.role.RoleRepository;
import com.example.sources.domain.repository.user.UserRepository;
import com.example.sources.exception.LoginFailedException;
import com.example.sources.exception.UserNotFoundException;
import com.example.sources.util.CookieUtil;
import com.example.sources.util.TokenUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final TokenUtil tokenUtil;
    private final CookieUtil cookieUtil;
    private final PasswordEncoder passwordEncoder;

    /**
     * username 과 email 을 받아 로그인한다.
     *
     * @param request Login 에 필요한 DTO
     * @return email, name 을 포함한 DTO
     */
    public LoginResponseData login(LoginRequestData request) {
        String email = request.getEmail();
        String password = request.getPassword();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException(email));

        boolean isAuthenticated = user.authenticate(password, passwordEncoder);

        if(!isAuthenticated) { // 로그인에 실패한 경우
            throw new LoginFailedException();
        }

        return modelMapper.map(user, LoginResponseData.class);
    }

    /**
     * 쿠키 배열로부터 토큰 String 을 반환한다.
     *
     * @param cookies : Request 에 포함된 쿠키 배열
     * @return token String
     */
    public String getTokenFromCookies(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("accessToken")) {
                return cookie.getValue();
            }
        }
        return "";
    }

    /**
     * token string 을 받아서 토큰을 parsing 하여 userId 를 반환한다.
     *
     * @param token
     * @return userId
     */
    public Long parseToken(String token) {
        Claims claims = tokenUtil.parseToken(token);
        return claims.get("userId", Long.class);
    }

    /**
     * 회원 id 를 받아 권한을 조회한다.
     *
     * @param userId
     * @return 권한 목록
     */
    public List<Role> getRoles(Long userId) {
        return roleRepository.findAllByUserId(userId);
    }
}
