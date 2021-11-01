package com.example.sources.domain.repository.assignment;

import com.example.sources.domain.dto.response.AssignmentResponseData;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

import java.util.List;

import static com.example.sources.domain.entity.QAssignment.assignment;

public class AssignmentRepositoryImpl implements AssignmentQuery {
    private final JPAQueryFactory queryFactory;

    public AssignmentRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<AssignmentResponseData> findAllByCourseId(Long courseId) {
        return queryFactory
                .select(Projections.fields(AssignmentResponseData.class,
                        assignment.id,
                        assignment.title,
                        assignment.startedAt,
                        assignment.endedAt))
                .from(assignment)
                .where(assignment.course.id.eq(courseId))
                .fetch();
    }
}
