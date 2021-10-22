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
import javax.servlet.http.HttpServletResponse;
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
     * @param response ServletResponse
     * @return email, name 을 포함한 DTO
     */
    public LoginResponseData login(LoginRequestData request, HttpServletResponse response) {
        String email = request.getEmail();
        String password = request.getPassword();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException(email));

        boolean isAuthenticated = user.authenticate(password, passwordEncoder);

        if(!isAuthenticated) { // 로그인에 실패한 경우
            throw new LoginFailedException();
        }

        response.addCookie(cookieUtil
                .generateCookie(tokenUtil.generateToken(user.getId()))); // response 에 쿠키 추가

        return modelMapper.map(user, LoginResponseData.class);
    }

    public void test() {

        String student1 = passwordEncoder.encode("student1");
        String student2 = passwordEncoder.encode("student2");
        String student3 = passwordEncoder.encode("student3");
        String student4 = passwordEncoder.encode("student4");
        String student5 = passwordEncoder.encode("student5");
        String teacher1 = passwordEncoder.encode("teacher1");
        String teacher2 = passwordEncoder.encode("teacher2");
        String teacher3 = passwordEncoder.encode("teacher3");
        String teacher4 = passwordEncoder.encode("teacher4");
        String teacher5 = passwordEncoder.encode("teacher5");

        System.out.println("student1 = " + student1);
        System.out.println("student2 = " + student2);
        System.out.println("student3 = " + student3);
        System.out.println("student4 = " + student4);
        System.out.println("student5 = " + student5);

        System.out.println("teacher1 = " + teacher1);
        System.out.println("teacher2 = " + teacher2);
        System.out.println("teacher3 = " + teacher3);
        System.out.println("teacher4 = " + teacher4);
        System.out.println("teacher5 = " + teacher5);

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
