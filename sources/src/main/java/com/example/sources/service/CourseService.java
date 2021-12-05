package com.example.sources.service;

import com.example.sources.domain.dto.aws.TaskInfo;
import com.example.sources.domain.dto.request.CreateCourseRequestData;
import com.example.sources.domain.dto.response.CreateCourseResponseData;
import com.example.sources.domain.dto.response.ParticipantResponseData;
import com.example.sources.domain.entity.Course;
import com.example.sources.domain.entity.CourseUser;
import com.example.sources.domain.entity.Role;
import com.example.sources.domain.entity.User;
import com.example.sources.domain.repository.course.CourseRepository;
import com.example.sources.domain.repository.courseuser.CourseUserRepository;
import com.example.sources.domain.repository.role.RoleRepository;
import com.example.sources.domain.repository.user.UserRepository;
import com.example.sources.domain.type.RoleType;
import com.example.sources.exception.AuthenticationFailedException;
import com.example.sources.exception.NotFoundException;
import com.example.sources.exception.UserNotFoundException;
import com.example.sources.util.AwsEcsUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseUserRepository courseUserRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final AwsEcsUtil awsEcsUtil;
    private final ModelMapper modelMapper;


    /**
     * 강사가 클래스를 생성한다.
     *
     * @param request : 클래스 생성 Request DTO
     * @return : 생성된 클래스의 정보가 담긴 DTO
     */
    public CreateCourseResponseData addCourse(CreateCourseRequestData request) {
        Long userId = request.getUserId();

        User teacher = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("uid: " + userId));
        List<Role> roles = roleRepository.findAllByUserId(userId);

        if(!roles.contains(new Role(RoleType.TEACHER))) { // 클래스 주인이 관리자나 강사가 아닌 경우에
            throw new AuthenticationFailedException();
        }

        Course course = modelMapper.map(request, Course.class);

        course.create(teacher); // 강사 설정

        Course savedCourse = courseRepository.save(course);

        TaskInfo taskInfo = awsEcsUtil.createEcsContainer();

        CourseUser courseUser = CourseUser.builder() // 클래스에 강사를 허용
                .course(savedCourse)
                .user(teacher)
                .build();

        courseUser.enrollTask(taskInfo);

        courseUserRepository.save(courseUser);

        CreateCourseResponseData response = modelMapper.map(savedCourse, CreateCourseResponseData.class);
        response.setCourseId(savedCourse.getId());
        return response;
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

    /**
     * 클래스를 삭제한다.
     *
     * @param courseId : 삭제하려는 클래스 id
     */
    public void deleteCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("클래스 번호 " + courseId));
        // TODO 클래스를 삭제하기 전에 question 에서 클래스에 해당하는 question 과 submit 을 먼저 삭제해야함
        courseRepository.delete(course);
    }

    /**
     * 학생을 초대하고 ecs 컨테이너를 생성한다.
     *
     * @param courseId
     * @param studentId
     */
    @Deprecated
    @Async("awsCliExecutor")
    public CourseUser inviteStudent(Long courseId, Long studentId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("클래스 번호 " + courseId));
        User student = userRepository.findById(studentId).orElseThrow(
                () -> new NotFoundException("사용자 번호 " + studentId));

        TaskInfo taskInfo = awsEcsUtil.createEcsContainer();

        CourseUser courseUser = CourseUser.builder()
                .user(student)
                .course(course)
                .build();

        courseUser.enrollTask(taskInfo);

        return courseUserRepository.save(courseUser);
    }

    /**
     * 강사가 특정 클래스에 소속된 모든 학생의 컨테이너 ip 를 조회한다.
     *
     * @param courseId : 조회할 클래스 id
     * @param tokenUserId : 요청을 보낸 강사의 userId
     * @return studentId, studentName, containerIp 가 담긴 DTO List
     */
    public List<ParticipantResponseData> getParticipants(Long courseId, Long tokenUserId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("클래스 번호 " + courseId));

        if(!course.isOwner(tokenUserId)) { // 강사가 아닌 경우
            throw new AuthenticationFailedException();
        }

        return courseUserRepository.findAllByCourseId(courseId);
    }
}
