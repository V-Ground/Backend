package com.example.sources.service;

import com.example.sources.domain.dto.request.CreateCourseRequestData;
import com.example.sources.domain.dto.response.CourseResponseData;
import com.example.sources.domain.dto.response.CreateCourseResponseData;
import com.example.sources.domain.entity.Course;
import com.example.sources.domain.entity.User;
import com.example.sources.domain.repository.course.CourseRepository;
import com.example.sources.domain.repository.coursestudent.CourseUserRepository;
import com.example.sources.domain.repository.user.UserRepository;
import com.example.sources.exception.AuthenticationFailedException;
import com.example.sources.exception.NotFoundException;
import com.example.sources.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    /**
     * 강사가 클래스를 생성한다.
     *
     * @param request : 클래스 생성 Request DTO
     * @return : 생성된 클래스의 정보가 담긴 DTO
     */
    public CreateCourseResponseData create(CreateCourseRequestData request) {
        Long userId = request.getUserId();

        User teacher = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("uid: " + userId));

        Course course = modelMapper.map(request, Course.class);
        course.create(teacher); // 강사 설정

        Course savedCourse = courseRepository.save(course);

        return modelMapper.map(savedCourse, CreateCourseResponseData.class);
    }

    /**
     * 클래스를 비활성화한다.
     *
     * @param courseId : 비활성화를 할 클래스 id
     */
    public void disableCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("클래스 번호 " + courseId));

        course.disable();

        courseRepository.save(course);
    }

}
