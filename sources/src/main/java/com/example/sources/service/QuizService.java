package com.example.sources.service;

import com.example.sources.domain.dto.request.CreateQuizRequestData;
import com.example.sources.domain.dto.request.SolveQuizRequestData;
import com.example.sources.domain.dto.response.CreateQuizResponseData;
import com.example.sources.domain.dto.response.SubmittedQuizResponseData;
import com.example.sources.domain.dto.response.QuizResponseData;
import com.example.sources.domain.entity.Evaluation;
import com.example.sources.domain.entity.EvaluationQuiz;
import com.example.sources.domain.entity.QuizSubmit;
import com.example.sources.domain.entity.User;
import com.example.sources.domain.repository.evaluation.EvaluationRepository;
import com.example.sources.domain.repository.evaluationquiz.EvaluationQuizRepository;
import com.example.sources.domain.repository.quizsubmit.QuizSubmitRepository;
import com.example.sources.domain.repository.user.UserRepository;
import com.example.sources.exception.AuthenticationFailedException;
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
    private final QuizSubmitRepository quizSubmitRepository;
    private final EvaluationRepository evaluationRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    /**
     * 평가에 해당하는 문제를 조회한다.
     *
     * @param evaluationId : 문제를 조회할 평가 번호
     * @return QuizResponseData List 의 DTO
     */
    public List<QuizResponseData> getAllQuizzes(Long evaluationId) {
        return evaluationQuizRepository.findAllByEvaluationId(evaluationId);
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

    /**
     * 사용자가 입력한 문제의 정답을 저장한다.
     *
     * @param quizId : 요청에 포함된 퀴즈의 quizId
     * @param request : 사용자의 정답 데이터
     * @param tokenUserId : 요청을 보낸 사용자의 userId
     */
    public void solveEvaluationQuiz(Long quizId, SolveQuizRequestData request, Long targetId, Long tokenUserId) {
        if(!targetId.equals(tokenUserId)) { // 제출하는 userId 와 저장하려는 userId 가 다른 경우
            throw new AuthenticationFailedException();
        }

        User user = userRepository.findById(tokenUserId).orElseThrow(
                () -> new NotFoundException("사용자 번호 " + tokenUserId));
        EvaluationQuiz evaluationQuiz = evaluationQuizRepository.findById(quizId).orElseThrow(
                () -> new NotFoundException("퀴즈 번호 " + quizId));

        String answer = request.getAnswer();
        QuizSubmit quizSubmit = QuizSubmit.builder()
                .answer(answer)
                .evaluationQuiz(evaluationQuiz)
                .user(user)
                .build();

        // TODO : 생성되지 않아도 void 로 결과를 검사하지 않으니 검증 로직이 추가될 필요가 있음
        quizSubmitRepository.save(quizSubmit);
    }

    /**
     * 특정 학생이 제출한 퀴즈 정답 확인
     *
     * @param
     * @return
     */
    public List<SubmittedQuizResponseData> getAllSubmittedQuizAnswer(Long teacherId, Long userId, Long evaluationId) {
        Evaluation evaluation = evaluationRepository.findById(evaluationId).orElseThrow(
                () -> new NotFoundException("테스트 번호 " + evaluationId));

        User teacher = evaluation.getTeacher();

        if(!teacherId.equals(teacher.getId())) { // 요청을 보낸 강사의 id 가 evaluation 의 소유자가 아닌 경우
            throw new AuthenticationFailedException();
        }
        return quizSubmitRepository.findAllByEvaluationIdAndUserId(evaluationId, userId);
    }
}
