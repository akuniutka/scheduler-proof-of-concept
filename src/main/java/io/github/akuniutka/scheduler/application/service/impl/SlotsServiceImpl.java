package io.github.akuniutka.scheduler.application.service.impl;

import io.github.akuniutka.scheduler.application.service.SlotService;
import io.github.akuniutka.scheduler.domain.model.Slot;
import io.github.akuniutka.scheduler.domain.repository.SlotRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class SlotsServiceImpl implements SlotService {

    private final SlotRepository repository;

    public SlotsServiceImpl(SlotRepository repository) {
        this.repository = repository;
    }

    @Override
    public void insertAll(Collection<Slot> slots) {
        repository.insertAll(slots);
    }

    @Override
    public List<Slot> findSlotsByEventIdOrderByStartTime(long eventId) {
        return repository.findAllByEventIdOrderByStartTime(eventId);
    }
}
