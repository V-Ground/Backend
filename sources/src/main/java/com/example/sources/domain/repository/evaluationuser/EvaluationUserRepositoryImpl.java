package com.example.sources.domain.repository.evaluationuser;

import com.example.sources.domain.dto.response.EvaluationResponseData;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.sources.domain.entity.QUser.user;
import static com.example.sources.domain.entity.QEvaluationUser.evaluationUser;
import static com.example.sources.domain.entity.QEvaluation.evaluation;

public class EvaluationUserRepositoryImpl implements EvaluationUserQuery {

    private final JPAQueryFactory queryFactory;

    public EvaluationUserRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<EvaluationResponseData> findAllByUserId(Long userId) {
        return queryFactory
                .select(getEvaluationListSelectFields())
                .from(evaluationUser)
                .innerJoin(evaluationUser.evaluation, evaluation)
                .innerJoin(evaluationUser.user, user)
                .on(user.id.eq(userId))
                .fetch();
    }

    @Override
    public Boolean existsByEvaluationIdAndUserId(Long evaluationId, Long userId) {
        Integer fetchOne = queryFactory
                .selectOne()
                .from(evaluationUser)
                .where(evaluationUser.evaluation.id.eq(evaluationId)
                        .and(evaluationUser.user.id.eq(userId)))
                .fetchFirst();

        return fetchOne != null;
    }

    private static QBean<EvaluationResponseData> getEvaluationListSelectFields() {
        return Projections.fields(EvaluationResponseData.class,
                evaluation.id.as("evaluationId"),
                evaluation.title,
                evaluation.description,
                evaluation.visibility,
                evaluation.teacher.username.as("teacherName"));
    }
}
