package com.example.sources.service;

import com.example.sources.domain.dto.request.CreateQuizRequestData;
import com.example.sources.domain.dto.response.CreateQuizResponseData;
import com.example.sources.domain.dto.response.QuizResponseData;
import com.example.sources.domain.entity.Evaluation;
import com.example.sources.domain.entity.EvaluationQuiz;
import com.example.sources.domain.repository.evaluation.EvaluationRepository;
import com.example.sources.domain.repository.evaluationquiz.EvaluationQuizRepository;
import com.example.sources.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class QuizService {
    private final EvaluationQuizRepository evaluationQuizRepository;
    private final EvaluationRepository evaluationRepository;
    private final ModelMapper modelMapper;

    /**
     * 평가에 해당하는 문제를 조회한다.
     *
     * @param evaluationId : 문제를 조회할 평가 번호
     * @return QuizResponseData List 의 DTO
     */
    public List<QuizResponseData> getAllQuizzes(Long evaluationId) {
        return evaluationQuizRepository.findAllByReferenceId(evaluationId);
    }

    /**
     * 평가에 해당하는 퀴즈를 생성한다.
     *
     * @param evaluationId : 문제를 추가할 평가 번호
     * @param request : List 타입의 CreateQuizRequestDto
     * @return 생성된 quiz 의 List 의 size
     */
    public CreateQuizResponseData addEvaluationQuiz(Long evaluationId, List<CreateQuizRequestData> request) {
        Evaluation evaluation = evaluationRepository.findById(evaluationId).orElseThrow(
                () -> new NotFoundException("평가 번호 : " + evaluationId));
        int count = 0;

        for (CreateQuizRequestData createQuizRequestData : request) {
            EvaluationQuiz quiz = modelMapper.map(createQuizRequestData, EvaluationQuiz.class);
            quiz.create(evaluation);
            evaluationQuizRepository.save(quiz);
            count++;
        }
        return new CreateQuizResponseData(count);
    }
}
