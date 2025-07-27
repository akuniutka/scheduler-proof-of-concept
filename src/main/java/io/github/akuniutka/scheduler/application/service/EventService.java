package io.github.akuniutka.scheduler.application.service;

import io.github.akuniutka.scheduler.domain.model.Event;

public interface EventService {

    void insert(Event event);

    Event getById(long id);
}
