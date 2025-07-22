package io.github.akuniutka.scheduler.domain.model;

import java.time.Instant;

public record Event(
        Long id,
        Long owner,
        Instant startTime,
        Instant endTime
) {
}
