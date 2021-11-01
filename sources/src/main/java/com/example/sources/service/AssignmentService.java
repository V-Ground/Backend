package com.example.sources.service;

import com.example.sources.domain.dto.request.CreateAssignmentRequestData;
import com.example.sources.domain.dto.request.CreateQuestionRequestData;
import com.example.sources.domain.dto.response.AssignmentResponseData;
import com.example.sources.domain.dto.response.CreateQuizResponseData;
import com.example.sources.domain.entity.Assignment;
import com.example.sources.domain.entity.Course;
import com.example.sources.domain.entity.CourseQuestion;
import com.example.sources.domain.repository.assignment.AssignmentRepository;
import com.example.sources.domain.repository.course.CourseRepository;
import com.example.sources.domain.repository.coursequestion.CourseQuestionQuery;
import com.example.sources.domain.repository.coursequestion.CourseQuestionRepository;
import com.example.sources.domain.repository.coursestudent.CourseUserRepository;
import com.example.sources.exception.AuthenticationFailedException;
import com.example.sources.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final CourseRepository courseRepository;
    private final CourseUserRepository courseUserRepository;
    private final CourseQuestionRepository courseQuestionRepository;
    private final ModelMapper modelMapper;

    /**
     * 특정 course 에 과제를 추가한다.
     *
     * @param courseId : 생성하려는 course 의 id
     * @param request : 과제 생성을 위한 데이터
     * @param tokenUserId : 토큰에 포함된 user 의 id
     * @return 생성된 과제의 정보가 담긴 DTO
     */
    public AssignmentResponseData addAssignment(Long courseId,
                                                CreateAssignmentRequestData request,
                                                Long tokenUserId) {
        Course course = courseRepository.findById(courseId).orElseThrow( // 과제를 생성할 course 가 없는 경우
                () -> new NotFoundException("클래스 번호 " + courseId));

        if(!course.isOwner(tokenUserId)) { // 동일한 사용자가 아닌 경우
            throw new AuthenticationFailedException();
        }

        Assignment assignment = modelMapper.map(request, Assignment.class);
        assignment.create(course);

        Assignment savedAssignment = assignmentRepository.save(assignment);

        return modelMapper.map(savedAssignment, AssignmentResponseData.class);
    }

    /**
     * course 에 포함된 모든 과제를 조회한다.
     *
     * @param courseId : 과제를 조회하려는 courseId
     * @param tokenUserId : 요청을 보낸 사용자의 userId
     * @return List 타입의 Assignment DTO
     */
    public List<AssignmentResponseData> getAssignments(Long courseId, Long tokenUserId) {
        courseRepository.findById(courseId).orElseThrow(
                () -> new NotFoundException("클래스 번호 " + courseId));

        Boolean isExists = courseUserRepository.existsByCourseIdAndUserId(courseId, tokenUserId);

        if(!isExists) { // 요청을 보낸 학생이 과제를 조회하려는 Course 에 소속되지 않은 경우
            throw new AuthenticationFailedException();
        }

        return assignmentRepository.findAllByCourseId(courseId);
    }

    public CreateQuizResponseData addQuestion(Long courseId,
                              Long assignmentId,
                              List<CreateQuestionRequestData> request,
                              Long tokenUserId) {

        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new NotFoundException("클래스 번호 " + courseId));

        if(!course.isOwner(tokenUserId)) { // 만약 클래스 소유자가 아닌 경우
            throw new AuthenticationFailedException();
        }

        Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow(
                () -> new NotFoundException("과제 번호 " + assignmentId));

        int createdCount = 0;

        for (CreateQuestionRequestData createQuestionRequestData : request) {
            CourseQuestion courseQuestion = modelMapper.map(request, CourseQuestion.class);
            courseQuestion.create(assignment);
            courseQuestionRepository.save(courseQuestion);
            createdCount++;
        }

        return new CreateQuizResponseData(createdCount);
    }
}
