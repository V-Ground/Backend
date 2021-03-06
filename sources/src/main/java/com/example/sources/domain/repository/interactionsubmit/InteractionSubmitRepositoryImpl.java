package com.example.sources.domain.repository.interactionsubmit;


import com.example.sources.domain.dto.response.InteractionSubmitResponseData;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.sources.domain.entity.QInteraction.interaction;
import static com.example.sources.domain.entity.QInteractionSubmit.interactionSubmit;
import static com.example.sources.domain.entity.QUser.user;

public class InteractionSubmitRepositoryImpl implements InteractionSubmitQuery {

    private final JPAQueryFactory queryFactory;

    public InteractionSubmitRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<InteractionSubmitResponseData> findAllByInteractionId(Long interactionId) {
        return queryFactory
                .select(Projections.fields(InteractionSubmitResponseData.class,
                        interactionSubmit.id.as("interactionSubmitId"),
                        interactionSubmit.yesNo.as("yesNo"),
                        interactionSubmit.answer,
                        interactionSubmit.user.username.as("studentName")
                        ))
                .from(interactionSubmit)
                .join(interactionSubmit.interaction, interaction)
                .where(interaction.id.eq(interactionId))
                .fetch();
    }

    @Override
    public List<InteractionSubmitResponseData> findAllByCourseIdAndUserId(Long courseId, Long userId) {
        return queryFactory
                .select(Projections.fields(InteractionSubmitResponseData.class,
                        interaction.id.as("interactionId"),
                        interactionSubmit.id.as("interactionSubmitId"),
                        interactionSubmit.yesNo.as("yesNo")))
                .from(interactionSubmit)
                .where(interactionSubmit.interaction.course.id.eq(courseId).and(interactionSubmit.user.id.eq(userId)))
                .fetch();
    }
}
