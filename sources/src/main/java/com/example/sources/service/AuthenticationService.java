package com.example.sources.service;

import com.example.sources.domain.dto.request.LoginRequestData;
import com.example.sources.domain.dto.response.LoginResponseData;
import com.example.sources.domain.entity.Role;
import com.example.sources.domain.entity.User;
import com.example.sources.domain.repository.role.RoleRepository;
import com.example.sources.domain.repository.user.UserRepository;
import com.example.sources.exception.LoginFailedException;
import com.example.sources.exception.NotFoundException;
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
    private final PasswordEncoder passwordEncoder;

    /**
     * username 과 email 을 받아 로그인한다.
     *
     * @param request Login 에 필요한 DTO
     * @return email, name, 권한을 포함한 DTO
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

        return getLoginResponseData(user);
    }

    /**
     * token 에 포함된 사용자의 정보를 토대로 값을 반환한다.
     *
     * @param tokenUserId : 요청에 포함된 userId
     * @return 로그인 성공 dto
     */
    public LoginResponseData validate(Long tokenUserId) {
        User user = userRepository.findById(tokenUserId)
                .orElseThrow(() -> new NotFoundException("사용자 번호 " + tokenUserId));

        return getLoginResponseData(user);
    }

    /**
     * user 객체의 권한을 조회하고 dto 에 권한을 추가하여 반환한다.
     *
     * @param user : 조회할 user
     * @return : 로그인 성공 dto
     */
    private LoginResponseData getLoginResponseData(User user) {
        List<Role> roles = roleRepository.findAllByUserId(user.getId());

        LoginResponseData responseData = modelMapper.map(user, LoginResponseData.class);
        int size = roles.size();

        String role;

        if(size == 3) {
            role = "관리자";
        }else if(size == 2) {
            role = "강사";
        }else {
            role = "학생";
        }
        responseData.setRole(role);
        return responseData;
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
