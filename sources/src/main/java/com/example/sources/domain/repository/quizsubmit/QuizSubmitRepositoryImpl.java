package com.example.sources.domain.repository.quizsubmit;

import com.example.sources.domain.dto.response.QuizResponseData;
import com.example.sources.domain.dto.response.SubmittedQuizResponseData;
import com.example.sources.domain.entity.EvaluationQuiz;
import com.example.sources.domain.entity.QQuizSubmit;
import com.example.sources.domain.entity.QuizSubmit;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

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
                        quizSubmit.id.as("answerId"),
                        evaluationQuiz.question,
                        quizSubmit.answer.as("submittedAnswer"),
                        quizSubmit.scored
                        ))
                .from(quizSubmit)
                .innerJoin(quizSubmit.evaluationQuiz, evaluationQuiz)
                .on(quizSubmit.evaluationQuiz.id.eq(evaluationQuiz.id))
                .where(quizSubmit.user.id.eq(userId).and(evaluationQuiz.evaluation.id.eq(evaluationId)))
                .fetch();
    }

    @Override
    public Optional<QuizSubmit> findByQuizIdAndUserId(Long quizId, Long userId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(quizSubmit)
                .where(quizSubmit.evaluationQuiz.id.eq(quizId).and(quizSubmit.user.id.eq(userId)))
                .fetchOne());
    }
}