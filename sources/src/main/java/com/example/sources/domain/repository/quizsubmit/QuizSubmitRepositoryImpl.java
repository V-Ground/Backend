package com.example.sources.domain.repository.quizsubmit;

import com.example.sources.domain.dto.response.SubmittedQuizResponseData;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.sources.domain.entity.QQuizSubmit.quizSubmit;
import static com.example.sources.domain.entity.QEvaluationQuiz.evaluationQuiz;
import static com.example.sources.domain.entity.QUser.user;
import static com.example.sources.domain.entity.QEvaluationUser.evaluationUser;
import static com.example.sources.domain.entity.QEvaluation.evaluation;

public class QuizSubmitRepositoryImpl implements QuizSubmitQuery {
    private final JPAQueryFactory queryFactory;

    public QuizSubmitRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<SubmittedQuizResponseData> findAllByEvaluationIdAndUserId(Long evaluationId, Long userId) {
        return queryFactory
                .select(Projections.fields(SubmittedQuizResponseData.class,
                        quizSubmit.id.as("quizId"),
                        evaluationQuiz.question,
                        quizSubmit.answer.as("submittedAnswer")
                        ))
                .from(quizSubmit)
                .innerJoin(quizSubmit.evaluationQuiz, evaluationQuiz)
                .on(quizSubmit.user.id.eq(userId))
                .innerJoin(evaluationQuiz.evaluation, evaluation)
                .on(evaluation.id.eq(evaluationId))
                .fetch();
    }
}

/**
 *
 * 강사가 Evaluation(평가)를 생성한다.
 * 강사는 문제(Evaluation_Quiz) 를 생성한다.
 * [Evaluation] 1 : N [Evaluation_Quiz] 가 된다.
 * 학생은 문제를 푼다. (Quiz_Submit 에 insert)
 *
 * 문제 : 학생 (1L) 이 평가 (1L) 에 제출한 정답과 문제 번호를 가져오는 select 쿼리를 짜세용
 * 문제 2 : quiz_submit 을 대상 테이블로 해서 문제 1을 해결하는 select 쿼리를 짜세용
 *
 * select
 * from evaluation
 *
 * @param
 * @return
 */
