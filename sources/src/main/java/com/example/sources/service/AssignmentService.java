package com.example.sources.service;

import com.example.sources.domain.dto.request.CreateAssignmentReqData;
import com.example.sources.domain.dto.request.CreateQuestionRequestData;
import com.example.sources.domain.dto.request.ScoringRequestData;
import com.example.sources.domain.dto.request.SolveQuestionRequestData;
import com.example.sources.domain.dto.response.*;
import com.example.sources.domain.entity.*;
import com.example.sources.domain.repository.assignment.AssignmentRepository;
import com.example.sources.domain.repository.course.CourseRepository;
import com.example.sources.domain.repository.coursequestion.CourseQuestionRepository;
import com.example.sources.domain.repository.courseuser.CourseUserRepository;
import com.example.sources.domain.repository.questionsubmit.QuestionSubmitRepository;
import com.example.sources.domain.repository.user.UserRepository;
import com.example.sources.exception.AuthenticationFailedException;
import com.example.sources.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final CourseRepository courseRepository;
    private final CourseUserRepository courseUserRepository;
    private final CourseQuestionRepository courseQuestionRepository;
    private final QuestionSubmitRepository questionSubmitRepository;
    private final UserRepository userRepository;
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
                                                CreateAssignmentReqData request,
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

    /**
     * 과제에 주관식 문제를 추가한다.
     *
     * @param courseId : 클래스의 id
     * @param assignmentId : 과제의 id
     * @param request : 추가할 주관식 문제의 Request DTO
     * @param tokenUserId : 요청을 보낸 사용자의 userId
     * @return 생성된 주관식 문제의 count
     */
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
            CourseQuestion courseQuestion = modelMapper.map(createQuestionRequestData, CourseQuestion.class);
            courseQuestion.create(assignment);
            courseQuestionRepository.save(courseQuestion);
            createdCount++;
        }

        return new CreateQuizResponseData(createdCount);
    }

    /**
     * 과제의 세부 내용을 조회하고 과제의 설명과 주관식 문제 list 를 반환한다.
     *
     * @param courseId : 클래스 번호
     * @param assignmentId : 조회하려는 과제의 번호
     * @param userId : 사용자 id
     * @param tokenUserId : 토큰에 포함된 사용자 id
     * @return
     */
    public QuestionWithSubmittedResponseData getAssignmentDetail(Long courseId,
                                                            Long assignmentId,
                                                            Long userId,
                                                            Long tokenUserId) {
        if(!userId.equals(tokenUserId)) {
            throw new AuthenticationFailedException();
        }

        Boolean isParticipant = courseUserRepository.existsByCourseIdAndUserId(courseId, tokenUserId);

        if(!isParticipant) {
            throw new AuthenticationFailedException();
        }

        AssignmentDetailResponseData questionDetail = courseQuestionRepository.findAssignmentDetailById(assignmentId).orElseThrow(
                () -> new NotFoundException("과제 번호 " + assignmentId));

        List<SubmittedQuestionResponseData> submittedAnswerDetail = questionSubmitRepository
                .findAllByAssignmentIdAndUserId(assignmentId, userId);

        return new QuestionWithSubmittedResponseData(questionDetail, submittedAnswerDetail);
    }

    /**
     * 학생이 과제를 제출한다.
     *
     * @param userId : 과제를 제출한 학생의 userId
     * @param courseId : 과제를 포함하는 courseId
     * @param assignmentId : 과제 번호
     * @param questionId : 주관식 번호
     * @param tokenUserId : 요청을 보내는 토큰에 포함된 userId
     * @return 제출한 정답의 DTO
     */
    public SolveQuestionRequestData solveQuestion(Long userId,
                                           Long courseId,
                                           Long assignmentId,
                                           Long questionId,
                                           SolveQuestionRequestData request,
                                           Long tokenUserId) {
        if(!userId.equals(tokenUserId)) {
            throw new AuthenticationFailedException();
        }

        courseRepository.findById(courseId).orElseThrow(
                () -> new NotFoundException("클래스 번호 " + courseId));
        assignmentRepository.findById(assignmentId).orElseThrow(
                () -> new NotFoundException("과제 번호 " + assignmentId));
        CourseQuestion courseQuestion = courseQuestionRepository.findById(questionId).orElseThrow(
                () -> new NotFoundException("주관식 번호 " + questionId));
        User user = userRepository.findById(tokenUserId).orElseThrow(
                () -> new NotFoundException("사용자 번호 " + userId));

        Boolean isMember = courseUserRepository.existsByCourseIdAndUserId(courseId, userId);

        if(!isMember) { // 클래스에 소속되지 않은 경우
            throw new AuthenticationFailedException();
        }

        Optional<QuestionSubmit> submit = questionSubmitRepository.findByQuestionIdAndUserId(questionId, userId);

        QuestionSubmit questionSubmit;

        if(submit.isEmpty()) { // 기존에 정답이 없는 경우
            questionSubmit = modelMapper.map(request, QuestionSubmit.class);
            questionSubmit.solve(courseQuestion, user);
        }else {
            questionSubmit = submit.get();
            questionSubmit.updateAnswer(request.getAnswer());
        }

        QuestionSubmit savedQuestionSubmit = questionSubmitRepository.save(questionSubmit);
        return modelMapper.map(savedQuestionSubmit, SolveQuestionRequestData.class);
    }

    /**
     * 특정 학생이 제출한 모든 과제의 정답을 확인한다.
     *
     * @param teacherId : 강사의 id
     * @param courseId : 강사가 소속된 course 의 id
     * @param assignmentId : 과제의 id
     * @param userId : 채점하려는 학생의 id
     * @param tokenUserId : 강사의 요청 토큰에 포함된 userId
     * @return 학생이 제출한 정답과 배점 DTO List
     */
    public List<SubmittedQuestionResponseData> getSubmittedQuestions(Long teacherId,
                                                                     Long courseId,
                                                                     Long assignmentId,
                                                                     Long userId,
                                                                     Long tokenUserId) {
        if(!teacherId.equals(tokenUserId)) {
            throw new AuthenticationFailedException();
        }
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new NotFoundException("클래스 번호 " + courseId));

        if(!course.isOwner(teacherId)) {
            throw new AuthenticationFailedException();
        }

        return questionSubmitRepository.findAllByAssignmentIdAndUserId(assignmentId, userId);
    }

    /**
     * 학생이 제출한 정답을 비교하여 과제를 체점한다.
     *
     * @param teacherId : 강사의 ID
     * @param courseId : 클래스 번호
     * @param assignmentId : 과제 번호
     * @param questionId : 주관식 문제 번호
     * @param request : 입력할 점수의 DTO
     * @param tokenUserId : 요청을 보낸 사용자의 id
     */
    public void scoreQuestion(Long teacherId,
                              Long courseId,
                              Long assignmentId,
                              Long questionId,
                              Long answerId,
                              ScoringRequestData request,
                              Long tokenUserId) {
        if (!teacherId.equals(tokenUserId)) {
            throw new AuthenticationFailedException();
        }
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new NotFoundException("클래스 번호 " + courseId));

        if (!course.isOwner(tokenUserId)) {
            throw new AuthenticationFailedException();
        }

        boolean assignmentExist = assignmentRepository.existsById(assignmentId);
        boolean questionExist = courseQuestionRepository.existsById(questionId);

        if(!assignmentExist || !questionExist) {
            throw new NotFoundException("과제 혹은 주관식 문제");
        }

        QuestionSubmit submitted = questionSubmitRepository.findById(answerId).orElseThrow(
                () -> new NotFoundException("제출 번호 " + answerId));

        submitted.scoring(request.getScore());

        questionSubmitRepository.save(submitted);
    }

    /**
     * 특정 과제에 대한 학생들의 정답을 요약하여 반환한다.
     *
     * @param courseId 클래스 번호
     * @param assignmentId 과제 번호
     * @param tokenUserId 토큰에 포함된 강사의 id
     * @return
     */
    public AssignmentSummaryResData getAssignmentSummary(Long courseId, Long assignmentId, Long tokenUserId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("클래스 번호" + courseId));

        boolean owner = course.isOwner(tokenUserId);

        if (!owner) {
            throw new AuthenticationFailedException();
        }

        List<CourseUser> courseUsers = courseUserRepository.findAllByCourseId(courseId);
        List<StudentSubmittedQuestionResData> submittedAnswers = new ArrayList<>();

        for (CourseUser courseUser : courseUsers) {
            Long userId = courseUser.getUser().getId();

            List<SubmittedQuestionResponseData> answers = questionSubmitRepository
                    .findAllByAssignmentIdAndUserId(assignmentId, userId);

            submittedAnswers.add(StudentSubmittedQuestionResData.builder()
                    .studentId(userId)
                    .submittedQuestions(answers)
                    .build());
        }

        List<QuestionResponseData> questions = courseQuestionRepository.findAllByAssignmentId(assignmentId);

        return AssignmentSummaryResData.builder()
                .questions(questions)
                .students(submittedAnswers)
                .build();

    }
}
