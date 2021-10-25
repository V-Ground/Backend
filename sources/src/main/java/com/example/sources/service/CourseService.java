package com.example.sources.service;

import com.example.sources.domain.dto.request.CreateCourseRequestData;
import com.example.sources.domain.dto.response.CreateCourseResponseData;
import com.example.sources.domain.entity.Course;
import com.example.sources.domain.entity.User;
import com.example.sources.domain.repository.course.CourseRepository;
import com.example.sources.domain.repository.user.UserRepository;
import com.example.sources.exception.AuthenticationFailedException;
import com.example.sources.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class CourseService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;

    /**
     * 강사가 클래스를 생성한다.
     *
     * @param request : 클래스 생성 Request DTO
     * @param tokenUserId : 토큰에 저장된 사용자 Id
     * @return : 생성된 클래스의 정보가 담긴 DTO
     */
    public CreateCourseResponseData create(CreateCourseRequestData request, Long tokenUserId) {
        User user = userRepository.findById(tokenUserId).orElseThrow(
                () -> new UserNotFoundException("uid: " + tokenUserId));

        Course course = modelMapper.map(request, Course.class);
        course.create(user); // 강사 설정

        return modelMapper.map(course, CreateCourseResponseData.class);
    }

}
