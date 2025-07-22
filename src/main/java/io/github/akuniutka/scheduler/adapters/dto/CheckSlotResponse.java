package io.github.akuniutka.scheduler.adapters.dto;

import io.github.akuniutka.scheduler.domain.model.Booking;

public record CheckSlotResponse(
        boolean hasIntersection,
        Booking booking,
        long timeSpent
) {
}
