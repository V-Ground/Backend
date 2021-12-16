package com.example.sources.domain.repository.questionsubmit;

import com.example.sources.domain.dto.response.QuestionWithSubmittedResponseData;
import com.example.sources.domain.dto.response.SubmittedQuestionResponseData;
import com.example.sources.domain.entity.QuestionSubmit;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.example.sources.domain.entity.QCourseQuestion.courseQuestion;
import static com.example.sources.domain.entity.QQuestionSubmit.questionSubmit;

public class QuestionSubmitRepositoryImpl implements QuestionSubmitQuery {
    private final JPAQueryFactory queryFactory;

    public QuestionSubmitRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<SubmittedQuestionResponseData> findAllByAssignmentIdAndUserId(Long assignmentId, Long userId) {

        return queryFactory
                .select(Projections.fields(SubmittedQuestionResponseData.class,
                        courseQuestion.id.as("questionId"),
                        courseQuestion.question,
                        questionSubmit.answer.as("submittedAnswer"),
                        questionSubmit.scored))
                .from(questionSubmit)
                .join(questionSubmit.question, courseQuestion)
                .where(courseQuestion.assignment.id.eq(assignmentId).and(questionSubmit.user.id.eq(userId)))
                .fetch();
    }

    @Override
    public List<SubmittedQuestionResponseData> findAllByCourseIdAndUserId(Long courseId, Long userId) {
        return queryFactory
                .select(Projections.fields(SubmittedQuestionResponseData.class,
                        courseQuestion.id.as("questionId"),
                        courseQuestion.question,
                        questionSubmit.answer.as("submittedAnswer"),
                        questionSubmit.scored))
                .from(questionSubmit)
                .join(questionSubmit.question, courseQuestion)
                .where(courseQuestion.assignment.course.id.eq(courseId).and(questionSubmit.user.id.eq(userId)))
                .fetch();
    }

    @Override
    public Optional<QuestionSubmit> findByQuestionIdAndUserId(Long questionId, Long userId) {
        return Optional.ofNullable(
                queryFactory
                .selectFrom(questionSubmit)
                .where(questionSubmit.question.id.eq(questionId).and(questionSubmit.user.id.eq(userId)))
                .fetchFirst());
    }
}