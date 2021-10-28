package com.example.sources.service;

import com.example.sources.domain.dto.response.CourseResponseData;
import com.example.sources.domain.entity.CourseUser;
import com.example.sources.domain.repository.coursestudent.CourseUserRepository;
import com.example.sources.domain.repository.user.UserRepository;
import com.example.sources.exception.AuthenticationFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final CourseUserRepository courseUserRepository;

    /**
     * userId 로 소속된 모든 Course 를 조회한다.
     *
     * @param targetUserId 조회하려는 사용자의 userId
     * @param tokenUserId 토큰에 저장된 userId
     * @return List 타입의 CourseResponseData DTO
     */
    public List<CourseResponseData> getCourses(Long targetUserId, Long tokenUserId) {
        if(targetUserId != tokenUserId) { // 요청을 보낸 userId 와 조회하려는 userId 가 다른 경우
            throw new AuthenticationFailedException();
        }

        return courseUserRepository.findAllByUserId(tokenUserId);
    }
}
