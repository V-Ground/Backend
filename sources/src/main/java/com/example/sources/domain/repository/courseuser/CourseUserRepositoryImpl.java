package com.example.sources.domain.repository.courseuser;

import com.example.sources.domain.dto.response.CourseResponseData;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.sources.domain.entity.QCourseUser.courseUser;
import static com.example.sources.domain.entity.QUser.user;
import static com.example.sources.domain.entity.QCourse.course;

public class CourseUserRepositoryImpl implements CourseUserQuery {

    private final JPAQueryFactory queryFactory;

    public CourseUserRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<CourseResponseData> findAllByUserId(Long userId) {
        return queryFactory
                .select(getCourseListSelectFields())
                .from(courseUser)
                .innerJoin(courseUser.user, user)
                .innerJoin(courseUser.course, course)
                .on(user.id.eq(userId))
                .fetch();
    }

    @Override
    public Boolean existsByCourseIdAndUserId(Long courseId, Long userId) {
        Integer fetchOne = queryFactory
                .selectOne()
                .from(courseUser)
                .where(courseUser.course.id.eq(courseId)
                        .and(courseUser.user.id.eq(userId)))
                .fetchFirst();

        return fetchOne != null;
    }

    private static QBean<CourseResponseData> getCourseListSelectFields() {
        return Projections.fields(CourseResponseData.class,
                course.id.as("courseId"),
                course.title.as("title"),
                course.description.as("description"),
                course.visibility.as("visibility"),
                course.teacher.username.as("teacherName"));
    }
}
