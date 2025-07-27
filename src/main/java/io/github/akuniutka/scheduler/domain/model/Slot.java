package io.github.akuniutka.scheduler.domain.model;

import java.time.Instant;

public record Slot(
        long id,
        long eventId,
        Instant startTime,
        Instant endTime
) {
}
