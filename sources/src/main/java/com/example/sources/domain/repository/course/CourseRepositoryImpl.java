package com.example.sources.domain.repository.course;

import com.example.sources.domain.dto.response.CourseResponseData;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.sources.domain.entity.QCourse.course;
import static com.example.sources.domain.entity.QUser.user;

public class CourseRepositoryImpl implements CourseQuery {

    private final JPAQueryFactory queryFactory;

    public CourseRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<CourseResponseData> findAllByUserId(Long userId) {
        return queryFactory
                .select(Projections.fields(CourseResponseData.class,
                        course.id.as("courseId"),
                        course.title,
                        course.description,
                        course.visibility,
                        course.teacher.username.as("teacherName")))
                .from(course)
                .join(course.teacher, user)
                .where(user.id.eq(userId))
                .fetch();
    }
}
