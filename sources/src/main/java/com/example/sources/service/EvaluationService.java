package com.example.sources.service;

import com.example.sources.domain.dto.request.CreateEvaluationRequestData;
import com.example.sources.domain.dto.response.CreateEvaluationResponseData;
import com.example.sources.domain.dto.response.QuizResponseData;
import com.example.sources.domain.entity.Evaluation;
import com.example.sources.domain.entity.EvaluationUser;
import com.example.sources.domain.entity.Role;
import com.example.sources.domain.entity.User;
import com.example.sources.domain.repository.evaluation.EvaluationRepository;
import com.example.sources.domain.repository.evaluationquiz.EvaluationQuizRepository;
import com.example.sources.domain.repository.evaluationuser.EvaluationUserRepository;
import com.example.sources.domain.repository.quizsubmit.QuizSubmitRepository;
import com.example.sources.domain.repository.role.RoleRepository;
import com.example.sources.domain.repository.user.UserRepository;
import com.example.sources.domain.type.RoleType;
import com.example.sources.exception.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class EvaluationService {
    private final EvaluationRepository evaluationRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final EvaluationUserRepository evaluationUserRepository;
    private final EvaluationQuizRepository evaluationQuizRepository;
    private final ModelMapper modelMapper;

    /**
     * 강사가 테스트를 생성한다.
     *
     * @param request : 테스트 생성 Request DTO
     * @return 생성된 테스트의 정보가 담긴 DTO
     */
    public CreateEvaluationResponseData addEvaluation(CreateEvaluationRequestData request) {
        Long userId = request.getUserId();
        User teacher = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("사용자 번호 " + userId));

        List<Role> roles = roleRepository.findAllByUserId(userId);

        if(!roles.contains(new Role(RoleType.TEACHER))) {
            throw new AuthenticationFailedException();
        }

        Evaluation evaluation = modelMapper.map(request, Evaluation.class);
        evaluation.create(teacher);

        Evaluation savedEvaluation = evaluationRepository.save(evaluation);

        EvaluationUser evaluationUser = EvaluationUser.builder()
                .evaluation(savedEvaluation)
                .user(teacher)
                .build();

        evaluationUserRepository.save(evaluationUser);

        return modelMapper.map(savedEvaluation, CreateEvaluationResponseData.class);
    }

    /**
     * 평가를 비활성화한다.
     *
     * @param evaluationId : 비활성화를 할 평가 id
     */
    public void disableEvaluation(Long evaluationId) {
        Evaluation evaluation = evaluationRepository.findById(evaluationId).orElseThrow(
                () -> new NotFoundException("테스트 번호 " + evaluationId));

        evaluation.disable();

        evaluationRepository.save(evaluation);
    }

    /**
     * 평가를 삭제한다.
     *
     * @param evaluationId : 삭제할 평가 id
     */
    public void deleteEvaluation(Long evaluationId) {
        Evaluation evaluation = evaluationRepository.findById(evaluationId).orElseThrow(
                () -> new NotFoundException("테스트 번호 " + evaluationId));
        // TODO 평가를 삭제하기 전에 question 에서 평가에 해당하는 question 과 submit 을 먼저 삭제해야함
        evaluationRepository.delete(evaluation);
    }

    public void invite_test_need_delete(Long evaluationId, Long studentId) {

        Evaluation evaluation = evaluationRepository.findById(evaluationId).orElseThrow(
                () -> new NotFoundException("평가 번호 " + evaluationId));

        User student = userRepository.findById(studentId).orElseThrow(
                () -> new NotFoundException("사용자 번호 " + studentId));

        EvaluationUser evaluationUser = EvaluationUser.builder()
                .evaluation(evaluation)
                .user(student)
                .build();
        evaluationUserRepository.save(evaluationUser);
    }

    /**
     * 테스트 문제 및 상세 정보를 조회한다.
     *
     * @param evaluationId : 조회 대상 테스트 id
     * @param tokenUserId : 요청을 보낸 사용자의 userId
     * @return
     */
    public List<QuizResponseData> getQuizzes(Long evaluationId, Long tokenUserId) {

        Evaluation evaluation = evaluationRepository.findById(evaluationId).orElseThrow(
                () -> new NotFoundException("테스트 번호 " + evaluationId));

        boolean isMember = evaluationUserRepository.existsByEvaluationIdAndUserId(evaluationId, tokenUserId);

        if(!isMember) {
            throw new AuthenticationFailedException();
        }

        LocalDateTime startedAt = evaluation.getStartedAt();
        LocalDateTime endedAt = evaluation.getEndedAt();

        if(LocalDateTime.now().isBefore(startedAt)) {
            throw new CurriculumNotOpenException("해당 테스트는 아직 시작되지 않았습니다");
        }else if(LocalDateTime.now().isAfter(endedAt)) {
            throw new CurriculumClosedException("해당 테스트는 종료되었습니다");
        }

        return evaluationQuizRepository.findAllByEvaluationId(evaluationId);
    }
}
