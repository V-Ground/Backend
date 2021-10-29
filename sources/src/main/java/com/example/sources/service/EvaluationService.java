package com.example.sources.service;

import com.example.sources.domain.dto.request.CreateEvaluationRequestData;
import com.example.sources.domain.dto.response.CreateEvaluationResponseData;
import com.example.sources.domain.entity.Evaluation;
import com.example.sources.domain.entity.User;
import com.example.sources.domain.repository.evaluation.EvaluationRepository;
import com.example.sources.domain.repository.user.UserRepository;
import com.example.sources.exception.NotFoundException;
import com.example.sources.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class EvaluationService {
    private final EvaluationRepository evaluationRepository;
    private final UserRepository userRepository;
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
                () -> new UserNotFoundException("uid: " + userId));

        Evaluation evaluation = modelMapper.map(request, Evaluation.class);
        evaluation.create(teacher);

        Evaluation savedEvaluation = evaluationRepository.save(evaluation);

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
}
