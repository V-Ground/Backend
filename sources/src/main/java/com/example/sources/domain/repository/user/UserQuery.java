package com.example.sources.domain.repository.user;

import com.example.sources.domain.entity.User;

import java.util.Optional;

public interface UserQuery {
    Optional<User> findByEmail(String email);
}
