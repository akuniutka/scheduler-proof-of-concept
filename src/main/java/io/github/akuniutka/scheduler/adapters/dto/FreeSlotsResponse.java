package io.github.akuniutka.scheduler.adapters.dto;

import io.github.akuniutka.scheduler.domain.model.Slot;

import java.util.List;

public record FreeSlotsResponse(
        long slotsFound,
        List<Slot> firstPage,
        long timeToLoadData,
        long timeToCalculate,
        long timeTotal
) {
}
