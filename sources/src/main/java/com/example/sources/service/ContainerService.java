package com.example.sources.service;

import com.example.sources.domain.dto.feign.StatusResponseData;
import com.example.sources.domain.dto.response.ContainerStatusResponseData;
import com.example.sources.domain.dto.response.ParticipantResponseData;
import com.example.sources.domain.entity.Course;
import com.example.sources.domain.repository.course.CourseRepository;
import com.example.sources.domain.repository.courseuser.CourseUserRepository;
import com.example.sources.exception.AuthenticationFailedException;
import com.example.sources.exception.NotFoundException;
import com.example.sources.exception.OpenFeignException;
import com.example.sources.feign.ContainerClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ContainerService {
    private final ContainerClient containerClient;
    private final CourseRepository courseRepository;
    private final CourseUserRepository courseUserRepository;

    /**
     * 학생들의 컨테이너에 파일이 존재하는지 확인한다.
     *
     * @param courseId
     * @param searchOption
     * @param baseDir
     * @param filename
     * @param tokenUserId
     * @return
     */
    public List<ContainerStatusResponseData> existFile(Long courseId,
                                                       Integer searchOption,
                                                       String baseDir,
                                                       String filename,
                                                       Long tokenUserId) {
        validateCourse(courseId, tokenUserId);

        List<ParticipantResponseData> courseUsers = courseUserRepository.findAllByCourseId(courseId);

        List<ContainerStatusResponseData> responseDataList = new ArrayList<>();

        for (ParticipantResponseData courseUser : courseUsers) {
            URI uri = URI.create(courseUser.getContainerIp() + ":8080");

            // 이거
            StatusResponseData responseData = containerClient.existsFile(uri, searchOption, baseDir, filename);

            // 이거

            ContainerStatusResponseData response = new ContainerStatusResponseData(courseUser.getStudentId(), courseUser.getStudentName(), responseData.getStatus());
            responseDataList.add(response);
        }

        return responseDataList;
    }
    /**
     * 요청을 보낸 사용자가 course 의 소유자인지 검증하는 공통 로직 메서드
     *
     * @param courseId 검증할 대상 course
     * @param tokenUserId 요청을 보낸 사용자의 userId
     */
    private void validateCourse(Long courseId, Long tokenUserId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("클래스 번호 " + courseId));

        boolean owner = course.isOwner(tokenUserId);
        if(!owner) {
            throw new AuthenticationFailedException();
        }
    }
}
