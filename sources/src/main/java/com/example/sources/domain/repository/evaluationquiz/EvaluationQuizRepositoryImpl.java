package com.example.sources.domain.repository.evaluationquiz;

import com.example.sources.domain.dto.response.QuizResponseData;
import com.example.sources.domain.entity.EvaluationQuiz;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.example.sources.domain.entity.QEvaluationQuiz.evaluationQuiz;
import static com.example.sources.domain.entity.QEvaluation.evaluation;

public class EvaluationQuizRepositoryImpl implements EvaluationQuizQuery {

    private final JPAQueryFactory queryFactory;

    public EvaluationQuizRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<QuizResponseData> findAllByEvaluationId(Long evaluationId) {
        return queryFactory
                .select(Projections.fields(QuizResponseData.class,
                        evaluationQuiz.id.as("quizId"),
                        evaluationQuiz.question,
                        evaluationQuiz.description,
                        evaluationQuiz.score))
                .from(evaluationQuiz)
                .innerJoin(evaluationQuiz.evaluation, evaluation)
                .where(evaluation.id.eq(evaluationId))
                .fetch();
    }
}
