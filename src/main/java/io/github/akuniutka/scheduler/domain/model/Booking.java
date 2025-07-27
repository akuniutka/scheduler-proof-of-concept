package io.github.akuniutka.scheduler.domain.model;

import java.time.Instant;

public record Booking(
        long id,
        long eventId,
        long ownerId,
        Instant startTime,
        Instant endTime
) {
}
