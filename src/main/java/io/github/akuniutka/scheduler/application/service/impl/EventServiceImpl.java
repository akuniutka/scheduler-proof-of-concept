package io.github.akuniutka.scheduler.application.service.impl;

import io.github.akuniutka.scheduler.adapters.exception.NotFoundException;
import io.github.akuniutka.scheduler.application.service.EventService;
import io.github.akuniutka.scheduler.domain.model.Event;
import io.github.akuniutka.scheduler.domain.repository.EventRepository;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository repository;

    public EventServiceImpl(EventRepository repository) {
        this.repository = repository;
    }

    @Override
    public void insert(Event event) {
        repository.insert(event);
    }

    @Override
    public Event getById(long id) {
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException("Event with id %s not found".formatted(id))
        );
    }
}
