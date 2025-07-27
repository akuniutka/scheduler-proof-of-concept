package io.github.akuniutka.scheduler.domain.repository;

import io.github.akuniutka.scheduler.domain.model.Event;

import java.util.Optional;

public interface EventRepository {

    void insert(Event event);

    Optional<Event> findById(long id);
}
