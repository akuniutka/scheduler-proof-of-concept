package io.github.akuniutka.scheduler.domain.repository;

import io.github.akuniutka.scheduler.domain.model.User;

public interface UserRepository {

    void insert(User user);
}
