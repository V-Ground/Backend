package com.example.sources.domain.repository.coursequestion;

import com.example.sources.domain.dto.response.AssignmentDetailResponseData;
import com.example.sources.domain.dto.response.AssignmentResponseData;
import com.example.sources.domain.dto.response.QuestionResponseData;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.example.sources.domain.entity.QAssignment.assignment;
import static com.example.sources.domain.entity.QCourseQuestion.courseQuestion;

public class CourseQuestionRepositoryImpl implements CourseQuestionQuery {

    private final JPAQueryFactory queryFactory;

    public CourseQuestionRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<AssignmentDetailResponseData> findAssignmentDetailById(Long assignmentId) {

        List<QuestionResponseData> fetch = queryFactory
                .select(Projections.fields(QuestionResponseData.class,
                        courseQuestion.id.as("questionId"),
                        courseQuestion.question,
                        courseQuestion.description,
                        courseQuestion.score))
                .from(courseQuestion)
                .join(courseQuestion.assignment, assignment)
                .where(assignment.id.eq(assignmentId))
                .fetch();

        AssignmentResponseData fetchOne = queryFactory
                .select(Projections.fields(AssignmentResponseData.class,
                        assignment.id.as("assignmentId"),
                        assignment.title,
                        assignment.description,
                        assignment.startedAt,
                        assignment.endedAt))
                .from(assignment)
                .where(assignment.id.eq(assignmentId))
                .fetchOne();

        if(fetchOne == null) {
            return Optional.empty();
        }

        return Optional.of(new AssignmentDetailResponseData(fetchOne, fetch));
    }

    @Override
    public List<QuestionResponseData> findAllByAssignmentId(Long assignmentId) {
        return queryFactory
                .select(Projections.fields(QuestionResponseData.class,
                        courseQuestion.id.as("questionId"),
                        courseQuestion.question))
                .from(courseQuestion)
                .where(courseQuestion.assignment.id.eq(assignmentId))
                .fetch();
    }
}
