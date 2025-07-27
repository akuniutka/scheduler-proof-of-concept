package io.github.akuniutka.scheduler.application.service.impl;

import io.github.akuniutka.scheduler.application.service.UserService;
import io.github.akuniutka.scheduler.domain.model.User;
import io.github.akuniutka.scheduler.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void insert(User user) {
        repository.insert(user);
    }
}
