package com.example.sources.service;

import com.example.sources.domain.dto.request.CreateInteractionReqData;
import com.example.sources.domain.dto.request.SolveInteractionRequestData;
import com.example.sources.domain.dto.response.*;
import com.example.sources.domain.entity.*;
import com.example.sources.domain.repository.course.CourseRepository;
import com.example.sources.domain.repository.courseuser.CourseUserRepository;
import com.example.sources.domain.repository.interaction.InteractionRepository;
import com.example.sources.domain.repository.interactionsubmit.InteractionSubmitRepository;
import com.example.sources.domain.repository.user.UserRepository;
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
public class InteractionService {
    private final InteractionRepository interactionRepository;
    private final InteractionSubmitRepository interactionSubmitRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CourseUserRepository courseUserRepository;
    private final ModelMapper modelMapper;


    /**
     * 강사가 상호작용을 추가한다.
     *
     * @param request 추가할 상호작용 관련 데이터
     * @param tokenUserId 요청을 보낸 사용자의 토큰에 포함된 userId
     * @return 생성된 상호작용의 데이터
     */
    public InteractionResponseData addInteraction(Long courseId,
                                                  CreateInteractionReqData request,
                                                  Long tokenUserId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("클래스 번호 " + courseId));

        boolean owner = course.isOwner(tokenUserId);

        if(!owner) {
            throw new AuthenticationFailedException();
        }

        Interaction map = modelMapper.map(request, Interaction.class);
        map.publish(course);

        Interaction savedInteraction = interactionRepository.save(map);

        return modelMapper.map(savedInteraction, InteractionResponseData.class);
    }

    /**
     * 모든 인터렉션을 조회한다.
     *
     * @param courseId 인터렉션을 조회할 대상 클래스
     * @param tokenUserId 요청을 보낸 강사의 userId
     * @return
     */
    public List<InteractionResponseData> getInteractions(Long courseId, Long tokenUserId) {
        Boolean exists = courseUserRepository.existsByCourseIdAndUserId(courseId, tokenUserId);

        if(!exists) {
            throw new AuthenticationFailedException();
        }
        return interactionRepository.findAllByCourseId(courseId);
    }

    /**
     * 학생이 인터렉션의 정답을 제출한다.
     *
     * @param interactionId 제출할 인터렉션의 ID
     * @param studentId 저장할 학생의 id
     * @param request 제출할 답의 dto
     * @param tokenUserId 요청을 보낸 학생의 userId
     */
    public void solveInteraction(Long interactionId,
                                 Long courseId,
                                 Long studentId,
                                 SolveInteractionRequestData request,
                                 Long tokenUserId) {
        if(!studentId.equals(tokenUserId)) {
            throw new AuthenticationFailedException();
        }

        boolean isMember = courseUserRepository.existsByCourseIdAndUserId(courseId, studentId);

        if(!isMember) {
            throw new AuthenticationFailedException();
        }

        Interaction interaction = interactionRepository.findById(interactionId)
                .orElseThrow(() -> new NotFoundException("인터렉션 번호 " + interactionId));

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("학생 번호 " + studentId));

        InteractionSubmit interactionSubmit = InteractionSubmit.builder()
                .yesNo(request.getYesNo())
                .interaction(interaction)
                .user(student)
                .build();

        interactionSubmitRepository.save(interactionSubmit);
    }

    /**
     * 특정 인터렉션에 대해 학생들이 제출한 모든 정답 확인
     *
     * @param interactionId 조회할 인터렉션의 id
     * @param courseId 인터렉션이 포함된 클래스 id
     * @param tokenUserId 요청을 보낸 강사의 id
     * @return
     */
    public List<InteractionSubmitResponseData> getInteractionAnswers(Long interactionId,
                                                                     Long courseId,
                                                                     Long tokenUserId) {
        validateCourse(courseId, tokenUserId);
        return interactionSubmitRepository.findAllByInteractionId(interactionId);
    }

    /**
     * 코스에 대한 인터렉션과 학생들이 제출한 정답들을 요약하여 번환한다.
     *
     * @param courseId 클래스 번호
     * @param tokenUserId 요청을 보낸 강사의 id
     * @return
     */
    public InteractionSummaryResData getInteractionSummary(Long courseId, Long tokenUserId) {
        List<InteractionResponseData> interactions = interactionRepository.findAllByCourseId(courseId);

        List<CourseUser> courseUsers = courseUserRepository.findAllByCourseId(courseId);
        List<StudentSubmittedInteractionResData> answers = new ArrayList<>();
        for (CourseUser courseUser : courseUsers) {
            Long userId = courseUser.getUser().getId();
            List<InteractionSubmitResponseData> submittedAnswers = interactionSubmitRepository
                    .findAllByCourseIdAndUserId(courseId, userId);

            answers.add(StudentSubmittedInteractionResData.builder()
                    .studentId(userId)
                    .submittedInteractions(submittedAnswers)
                    .build());
        }
        return InteractionSummaryResData.builder()
                .students(answers)
                .interactions(interactions)
                .build();

    }

    /**
     * 요청을 보낸 사용자가 course 의 소유자인지 검증하는 공통 로직 메서드
     *
     * @param courseId 검증할 대상 course
     * @param tokenUserId 요청을 보낸 사용자의 userId
     * @return 조회된 Course
     */
    private Course validateCourse(Long courseId, Long tokenUserId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("클래스 번호 " + courseId));

        boolean owner = course.isOwner(tokenUserId);
        if(!owner) {
            throw new AuthenticationFailedException();
        }

        return course;
    }
}
