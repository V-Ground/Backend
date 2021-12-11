package com.example.sources.service;

import com.example.sources.domain.dto.feign.*;
import com.example.sources.domain.dto.request.ContainerBaseField;
import com.example.sources.domain.dto.request.ContainerFileReqData;
import com.example.sources.domain.dto.request.ContainerInstallReqData;
import com.example.sources.domain.dto.request.ContainerCommandReqData;
import com.example.sources.domain.dto.response.*;
import com.example.sources.domain.entity.Course;
import com.example.sources.domain.entity.CourseUser;
import com.example.sources.domain.repository.course.CourseRepository;
import com.example.sources.domain.repository.courseuser.CourseUserRepository;
import com.example.sources.exception.AuthenticationFailedException;
import com.example.sources.exception.NotFoundException;
import com.example.sources.feign.ContainerClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ContainerService {
    private final ContainerClient containerClient;
    private final CourseRepository courseRepository;
    private final CourseUserRepository courseUserRepository;

    /**
     * 컨테이너에 원격 명령을 실행한다.
     *
     * @param courseId 클래스 번호
     * @param requestData 원격 명령을 실행할 command dto
     * @param tokenUserId 요청을 보낸 강사의 userId
     * @return
     */
    public List<ContainerBashResData> executeRemoteCommand(Long courseId,
                                                           ContainerCommandReqData requestData,
                                                           Long tokenUserId) {
        validateCourse(courseId, tokenUserId);
        List<CourseUser> courseUsers = getCourseUserFromIds(requestData.getStudentIds());

        return courseUsers.stream()
                .map(courseUser ->
                        CompletableFuture.supplyAsync(
                                () -> {
                                    FeignBashResponseData feignResponse;
                                    URI uri = URI.create("http://" + courseUser.getContainerIp() + ":8080/command/execute");
                                    feignResponse = containerClient.executeRemoteCommand(
                                            uri,
                                            new FeignCommandReqData(requestData.getCommand()));

                                    return ContainerBashResData.builder()
                                            .studentId(courseUser.getId())
                                            .commandResult(feignResponse.getCommandResult())
                                            .build();
                                })
                                .orTimeout(2L, TimeUnit.SECONDS)
                                .exceptionally(e ->
                                        ContainerBashResData.builder()
                                                .studentId(courseUser.getId())
                                                .error(true)
                                                .build()))
                .collect(Collectors.toList())
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toUnmodifiableList());
    }

    public List<ContainerInstallResData> detectInstallation(Long courseId,
                                                            ContainerInstallReqData requestData,
                                                            Long tokenUserId) {
        validateCourse(courseId, tokenUserId);
        List<CourseUser> courseUsers = getCourseUserFromIds(requestData.getStudentIds());
        return courseUsers.stream()
                .map(courseUser ->
                        CompletableFuture.supplyAsync(
                                () -> {
                                    URI uri = URI.create("http://" + courseUser.getContainerIp() + ":8080/filesystem/file_install/");
                                    FeignInstallResData feignResponse = containerClient.detectInstallation(
                                            uri,
                                            requestData.getProgramName());

                                    return ContainerInstallResData.builder()
                                            .studentId(courseUser.getId())
                                            .status(feignResponse.getStatus())
                                            .installPath(feignResponse.getInstallPath())
                                            .version(feignResponse.getVersion())
                                            .build();
                                })
                                .orTimeout(2L, TimeUnit.SECONDS)
                                .exceptionally(e ->
                                        ContainerInstallResData.builder()
                                                .studentId(courseUser.getId())
                                                .error(true)
                                                .build()))
                .collect(Collectors.toList())
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toUnmodifiableList());
    }
    /**
     * 컨테이너 내부의 path 에 위치한 파일 내용을 반환한다.
     *
     * @param courseId 클래스 id
     * @param requestData 파일 확인을 위한 dto
     * @param tokenUserId 요청을 보낸 강사의 id
     * @return
     */
    public List<ContainerFileResData> getFileContent(Long courseId, ContainerFileReqData requestData, Long tokenUserId) {

        validateCourse(courseId, tokenUserId);
        List<CourseUser> courseUsers = getCourseUserFromIds(requestData.getStudentIds());

        return courseUsers.stream()
                .map(courseUser ->
                        CompletableFuture.supplyAsync(
                                () -> {
                                    URI uri = URI.create("http://" + courseUser.getContainerIp() + ":8080/filesystem/file_view");
                                    FeignFileResData feignResponse = containerClient.getFileContent(
                                            uri,
                                            requestData.getFilePath());

                                    return ContainerFileResData.builder()
                                            .studentId(courseUser.getId())
                                            .status(feignResponse.getStatus())
                                            .fileContent(feignResponse.getFileContent())
                                            .build();
                                })
                                .orTimeout(2L, TimeUnit.SECONDS)
                                .exceptionally(e ->
                                        ContainerFileResData.builder()
                                                .studentId(courseUser.getId())
                                                .error(true)
                                                .build()))
                .collect(Collectors.toList())
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * 컨테이너에 존재하는 bash_history 를 반환한다.
     *
     * @param courseId 클래스 id
     * @param requestData 요청 dto
     * @param tokenUserId 요청을 보낸 강사의 id
     * @return
     */
    public List<ContainerFileResData> getBashHistory(Long courseId, ContainerBaseField requestData, Long tokenUserId) {

        validateCourse(courseId, tokenUserId);
        List<CourseUser> courseUsers = getCourseUserFromIds(requestData.getStudentIds());

        return courseUsers.stream()
                .map(courseUser ->
                        CompletableFuture.supplyAsync(
                                () -> {
                                    URI uri = URI.create("http://" + courseUser.getContainerIp() + ":8080/bash_history/non_realtime");
                                    FeignHistoryResData feignResponse = containerClient.getBashHistory(uri);

                                    return ContainerFileResData.builder()
                                            .studentId(courseUser.getId())
                                            .fileContent(feignResponse.getFileContent())
                                            .build();
                                })
                                .orTimeout(2L, TimeUnit.SECONDS)
                                .exceptionally(e ->
                                        ContainerFileResData.builder()
                                                .studentId(courseUser.getId())
                                                .error(true)
                                                .build()))
                .collect(Collectors.toList())
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toUnmodifiableList());
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

    /**
     * 학생들 id 리스트를 받아 CourseUser 를 반환하는 공통 로직 메서드
     *
     * @param ids CourseUser 에 조회할 id list
     * @return 조회된 CourseUser 객체 리스트
     */
    private List<CourseUser> getCourseUserFromIds(List<Long> ids) {
        List<CourseUser> courseUsers = new ArrayList<>();

        for (Long studentId : ids) {
            CourseUser courseUser = courseUserRepository.findById(studentId)
                    .orElseThrow(() -> new NotFoundException("학생 번호 " + studentId));

            courseUsers.add(courseUser);
        }

        return courseUsers;
    }


}
