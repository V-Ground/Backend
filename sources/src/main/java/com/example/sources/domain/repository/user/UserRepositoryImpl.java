package com.example.sources.domain.repository.user;

import com.example.sources.domain.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.Optional;

import static com.example.sources.domain.entity.QUser.user;

public class UserRepositoryImpl implements UserQuery {

    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(queryFactory
                .selectFrom(user)
                .where(user.email.eq(email))
                .fetchOne());
    }
}
