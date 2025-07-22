package io.github.akuniutka.scheduler.domain.repository;

import io.github.akuniutka.scheduler.domain.model.Booking;

import java.time.Instant;
import java.util.Optional;

public interface BookingRepository {

    void insertAll(Booking[] bookings);

    Optional<Booking> checkSlot(Long ownerId, Instant startTime, Instant endTime);
}
