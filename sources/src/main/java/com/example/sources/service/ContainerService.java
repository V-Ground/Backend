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
                                            .studentId(courseUser.getUser().getId())
                                            .mouseActivation(feignStatusResData.getStatus() == 1)
                                            .build();
                                })
                                .orTimeout(2L, TimeUnit.SECONDS)
                                .exceptionally(e ->
                                        ContainerActivationResData.builder()
                                                .studentId(courseUser.getUser().getId())
                                                .error(true)
                                                .build()))
                .collect(Collectors.toList())
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * ??????????????? ?????? ????????? ????????????.
     *
     * @param courseId ????????? ??????
     * @param requestData ?????? ????????? ????????? command dto
     * @param tokenUserId ????????? ?????? ????????? userId
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
                                            .studentId(courseUser.getUser().getId())
                                            .commandResult(feignResponse.getCommandResult())
                                            .build();
                                })
                                .orTimeout(2L, TimeUnit.SECONDS)
                                .exceptionally(e ->
                                        ContainerBashResData.builder()
                                                .studentId(courseUser.getUser().getId())
                                                .error(true)
                                                .build()))
                .collect(Collectors.toList())
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * ??????????????? ?????? ??????????????? ???????????? ????????????.
     *
     * @param courseId ????????? ??????
     * @param studentIds ???????????? ???????????? id
     * @param scriptFile ???????????? scriptFile
     * @param tokenUserId ????????? ?????? ????????? id
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
                                            .studentId(courseUser.getUser().getId())
                                            .commandResult(feignResponse.getCommandResult())
                                            .build();
                                })
                                .orTimeout(2L, TimeUnit.SECONDS)
                                .exceptionally(e ->
                                        ContainerBashResData.builder()
                                                .studentId(courseUser.getUser().getId())
                                                .error(true)
                                                .build()))
                .collect(Collectors.toList())
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * ???????????? ????????? ?????? ???????????? ???????????? ?????????????????? ????????????.
     *
     * @param courseId ????????? ??????
     * @param requestData ????????? ????????? ?????? dto
     * @param tokenUserId ????????? ?????? ????????? id
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
                                            .studentId(courseUser.getUser().getId())
                                            .status(feignResponse.getStatus())
                                            .installPath(feignResponse.getInstallPath())
                                            .version(feignResponse.getVersion())
                                            .build();
                                })
                                .orTimeout(5L, TimeUnit.SECONDS)
                                .exceptionally(e ->
                                        ContainerInstallResData.builder()
                                                .studentId(courseUser.getUser().getId())
                                                .error(true)
                                                .build()))
                .collect(Collectors.toList())
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * ???????????? ????????? path ??? ????????? ?????? ????????? ????????????.
     *
     * @param courseId ????????? id
     * @param requestData ?????? ????????? ?????? dto
     * @param tokenUserId ????????? ?????? ????????? id
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
                                            .studentId(courseUser.getUser().getId())
                                            .status(feignResponse.getStatus())
                                            .fileContent(feignResponse.getFileContent())
                                            .build();
                                })
                                .orTimeout(2L, TimeUnit.SECONDS)
                                .exceptionally(e ->
                                        ContainerFileResData.builder()
                                                .studentId(courseUser.getUser().getId())
                                                .error(true)
                                                .build()))
                .collect(Collectors.toList())
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * ??????????????? ???????????? bash_history ??? ????????????.
     *
     * @param courseId ????????? id
     * @param requestData ?????? dto
     * @param tokenUserId ????????? ?????? ????????? id
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
                                            .studentId(courseUser.getUser().getId())
                                            .fileContent(feignResponse.getFileContent())
                                            .build();
                                })
                                .orTimeout(2L, TimeUnit.SECONDS)
                                .exceptionally(e ->
                                        ContainerFileResData.builder()
                                                .studentId(courseUser.getUser().getId())
                                                .error(true)
                                                .build()))
                .collect(Collectors.toList())
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * ???????????? ????????? ????????? ???????????? ????????? ????????????.
     *
     * @param courseId ????????? ??????
     * @param studentIds ???????????? ???????????? id list
     * @param inputFile ????????? ??????
     * @param filePath ????????? ??????
     * @param insertOption ?????? ?????? ?????? || 0 : ????????????, 1 : ????????? + random str ??? ????????? ?????? ??????
     * @param tokenUserId ????????? ID
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
                                            .studentId(courseUser.getUser().getId())
                                            .filename(feignResponse.getFilename())
                                            .savedPath(feignResponse.getSavedPath())
                                            .status(feignResponse.getStatus())
                                            .build();
                                })
                                .orTimeout(2L, TimeUnit.SECONDS)
                                .exceptionally(e ->
                                        ContainerFileInsertResData.builder()
                                                .studentId(courseUser.getUser().getId())
                                                .error(true)
                                                .build()))
                .collect(Collectors.toList())
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * ????????? ?????? ???????????? course ??? ??????????????? ???????????? ?????? ?????? ?????????
     *
     * @param courseId ????????? ?????? course
     * @param tokenUserId ????????? ?????? ???????????? userId
     */
    private void validateCourse(Long courseId, Long tokenUserId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("????????? ?????? " + courseId));

        boolean owner = course.isOwner(tokenUserId);
        if(!owner) {
            throw new AuthenticationFailedException();
        }
    }

    /**
     * ????????? id ???????????? ?????? CourseUser ??? ???????????? ?????? ?????? ?????????
     *
     * @param ids CourseUser ??? ????????? id list
     * @return ????????? CourseUser ?????? ?????????
     */
    private List<CourseUser> getCourseUserFromIds(Long courseId, List<Long> ids) {
        List<CourseUser> courseUsers = new ArrayList<>();

        for (Long studentId : ids) {
            // TODO ????????? ????????? ??????????????? ???????????? ???????????? exception throw ????????? exception ??? ?????? ?????? result ??? ????????? ??? ????????? ??? ???.
            CourseUser courseUser = courseUserRepository.findByCourseIdAndUserId(courseId, studentId)
                    .orElseThrow(() -> new NotFoundException("?????? ?????? " + studentId));

            courseUsers.add(courseUser);
        }

        return courseUsers;
    }

}
