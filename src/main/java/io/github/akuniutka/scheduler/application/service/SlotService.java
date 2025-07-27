package io.github.akuniutka.scheduler.application.service;

import io.github.akuniutka.scheduler.domain.model.Slot;

import java.util.Collection;
import java.util.List;

public interface SlotService {

    void insertAll(Collection<Slot> slots);

    List<Slot> findSlotsByEventIdOrderByStartTime(long eventId);
}
