package com.example.sources.domain.repository.interaction;

import com.example.sources.domain.dto.response.InteractionResponseData;
import com.example.sources.domain.entity.Interaction;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.sources.domain.entity.QInteraction.interaction;
import static com.example.sources.domain.entity.QCourse.course;

public class InteractionRepositoryImpl implements InteractionQuery {

    private final JPAQueryFactory queryFactory;

    public InteractionRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<InteractionResponseData> findAllByCourseId(Long courseId) {
        return queryFactory
                .select(Projections.fields(InteractionResponseData.class,
                        interaction.id.as("interactionId"),
                        interaction.title,
                        interaction.interactionType,
                        interaction.createdAt))
                .from(interaction)
                .join(interaction.course, course)
                .where(interaction.course.id.eq(course.id))
                .fetch();
    }
}
