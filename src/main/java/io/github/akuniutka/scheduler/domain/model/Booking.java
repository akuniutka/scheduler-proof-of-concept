package io.github.akuniutka.scheduler.domain.model;

import java.time.Instant;

public record Booking(
        Long id,
        Long eventId,
        Long ownerId,
        Instant startTime,
        Instant endTime
) {
}
