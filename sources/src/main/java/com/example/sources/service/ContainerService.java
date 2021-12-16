package com.example.sources.service;

import com.example.sources.domain.dto.feign.*;
import com.example.sources.domain.dto.request.*;
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
import org.springframework.web.multipart.MultipartFile;

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

    public List<ContainerActivationResData> detectActivation(Long courseId,
                                                             Long tokenUserId) {

        validateCourse(courseId, tokenUserId);
        List<CourseUser> courseUsers = courseUserRepository.findAllByCourseId(courseId);

        return courseUsers.stream()
                .map(courseUser ->
                        CompletableFuture.supplyAsync(
                                () -> {
                                    URI uri = URI.create("http://" + courseUser.getContainerIp() + ":8080");
                                    boolean activation = false;

                                    FeignActivationResData feignStatusResData = containerClient.detectKeyboardHit(uri);

                                    return ContainerActivationResData.builder()
                                            .studentId(courseUser.getId())
                                            .mouseActivation(feignStatusResData.getStatus() == 1)
                                            .build();
                                })
                                .orTimeout(2L, TimeUnit.SECONDS)
                                .exceptionally(e ->
                                        ContainerActivationResData.builder()
                                                .studentId(courseUser.getId())
                                                .error(true)
                                                .build()))
                .collect(Collectors.toList())
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toUnmodifiableList());
    }


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
        List<CourseUser> courseUsers = getCourseUserFromIds(courseId, requestData.getStudentIds());

        return courseUsers.stream()
                .map(courseUser ->
                        CompletableFuture.supplyAsync(
                                () -> {
                                    FeignBashResponseData feignResponse;
                                    URI uri = URI.create("http://" + courseUser.getContainerIp() + ":8080");
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

    /**
     * 컨테이너에 원격 스크립트를 삽입하고 실행한다.
     *
     * @param courseId 클래스 번호
     * @param studentIds 포함시킬 학생들의 id
     * @param scriptFile 실행시킬 scriptFile
     * @param tokenUserId 요청을 보낸 강사의 id
     * @return
     */
    public List<ContainerBashResData> executeRemoteScript(Long courseId,
                                                          List<Long> studentIds,
                                                          MultipartFile scriptFile,
                                                          Long tokenUserId) {
        validateCourse(courseId, tokenUserId);
        List<CourseUser> courseUsers = getCourseUserFromIds(courseId, studentIds);

        return courseUsers.stream()
                .map(courseUser ->
                        CompletableFuture.supplyAsync(
                                () -> {
                                    URI uri = URI.create("http://" + courseUser.getContainerIp() + ":8080");
                                    FeignBashResponseData feignResponse = containerClient.executeRemoteScript(
                                            uri,
                                            scriptFile);

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

    /**
     * 컨테이너 내부에 특정 파일이나 패키지가 설치되었는지 확인한다.
     *
     * @param courseId 클래스 번호
     * @param requestData 패키지 확인을 위한 dto
     * @param tokenUserId 요청을 보낸 강사의 id
     * @return
     */
    public List<ContainerInstallResData> detectInstallation(Long courseId,
                                                            ContainerInstallReqData requestData,
                                                            Long tokenUserId) {
        validateCourse(courseId, tokenUserId);
        List<CourseUser> courseUsers = getCourseUserFromIds(courseId, requestData.getStudentIds());
        return courseUsers.stream()
                .map(courseUser ->
                        CompletableFuture.supplyAsync(
                                () -> {
                                    URI uri = URI.create("http://" + courseUser.getContainerIp() + ":8080");
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
    public List<ContainerFileResData> getFileContent(Long courseId,
                                                     ContainerFileReqData requestData,
                                                     Long tokenUserId) {

        validateCourse(courseId, tokenUserId);
        List<CourseUser> courseUsers = getCourseUserFromIds(courseId, requestData.getStudentIds());

        return courseUsers.stream()
                .map(courseUser ->
                        CompletableFuture.supplyAsync(
                                () -> {
                                    URI uri = URI.create("http://" + courseUser.getContainerIp() + ":8080");
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
    public List<ContainerFileResData> getBashHistory(Long courseId,
                                                     ContainerHistoryReqData requestData,
                                                     Long tokenUserId) {
        validateCourse(courseId, tokenUserId);
        List<CourseUser> courseUsers = getCourseUserFromIds(courseId, requestData.getStudentIds());

        return courseUsers.stream()
                .map(courseUser ->
                        CompletableFuture.supplyAsync(
                                () -> {
                                    URI uri = URI.create("http://" + courseUser.getContainerIp() + ":8080");

                                    FeignHistoryResData feignResponse = containerClient.getBashHistory(
                                            uri,
                                            new FeignHistoryReqData(requestData.getExcludes()));

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
     * 컨테이너 내부로 파일을 주입하고 결과를 반환한다.
     *
     * @param courseId 클래스 번호
     * @param studentIds 포함시킬 학생들의 id list
     * @param inputFile 주입할 파일
     * @param filePath 파일의 경로
     * @param insertOption 파일 주입 옵션 || 0 : 덮어쓰기, 1 : 파일명 + random str 로 새로운 파일 생성
     * @param tokenUserId 강사의 ID
     * @return
     */
    public List<ContainerFileInsertResData> insertFile(Long courseId,
                                                 List<Long> studentIds,
                                                 String filePath,
                                                 Integer insertOption,
                                                 MultipartFile inputFile,
                                                 Long tokenUserId) {
        validateCourse(courseId, tokenUserId);
        List<CourseUser> courseUsers = getCourseUserFromIds(courseId, studentIds);

        return courseUsers.stream()
                .map(courseUser ->
                        CompletableFuture.supplyAsync(
                                () -> {
                                    URI uri = URI.create("http://" + courseUser.getContainerIp() + ":8080");
                                    FeignInsertFileResData feignResponse = containerClient.insertFile(
                                            uri,
                                            filePath,
                                            insertOption,
                                            inputFile);

                                    return ContainerFileInsertResData.builder()
                                            .studentId(courseUser.getId())
                                            .filename(feignResponse.getFilename())
                                            .savedPath(feignResponse.getSavedPath())
                                            .status(feignResponse.getStatus())
                                            .build();
                                })
                                .orTimeout(2L, TimeUnit.SECONDS)
                                .exceptionally(e ->
                                        ContainerFileInsertResData.builder()
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
    private List<CourseUser> getCourseUserFromIds(Long courseId, List<Long> ids) {
        List<CourseUser> courseUsers = new ArrayList<>();

        for (Long studentId : ids) {
            // TODO 현재는 하나의 학생이라도 존재하지 않는다면 exception throw 하는데 exception 에 따라 다른 result 는 반환할 수 있도록 할 것.
            CourseUser courseUser = courseUserRepository.findByCourseIdAndUserId(courseId, studentId)
                    .orElseThrow(() -> new NotFoundException("학생 번호 " + studentId));

            courseUsers.add(courseUser);
        }

        return courseUsers;
    }

}
