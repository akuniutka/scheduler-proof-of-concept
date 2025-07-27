package io.github.akuniutka.scheduler.domain.repository;

import io.github.akuniutka.scheduler.domain.model.Slot;

import java.util.Collection;
import java.util.List;

public interface SlotRepository {

    void insertAll(Collection<Slot> slots);

    List<Slot> findAllByEventIdOrderByStartTime(long eventId);
}
