package com.example.sources.service;

import com.example.sources.domain.dto.request.LoginRequestData;
import com.example.sources.domain.dto.response.LoginResponseData;
import com.example.sources.domain.entity.Role;
import com.example.sources.domain.repository.role.RoleRepository;
import com.example.sources.domain.repository.user.UserRepository;
import com.example.sources.util.TokenUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenUtil tokenUtil;

    /**
     * username 과 email 을 받아 로그인한다.
     *
     * @param
     * @return
     */
    public LoginResponseData login(LoginRequestData request) {
        String email = request.getEmail();
        String password = request.getPassword();
        return null;
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
