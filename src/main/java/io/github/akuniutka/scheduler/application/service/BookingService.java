package io.github.akuniutka.scheduler.application.service;

import io.github.akuniutka.scheduler.domain.model.Booking;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BookingService {

    void insertAll(Collection<Booking> bookings);

    Optional<Booking> findAnyByOwnerIdBetweenStartTimeAndEndTime(long ownerId, Instant startTime, Instant endTime);

    List<Booking> findAllByOwnerIdOrderByStartTime(long ownerId);
}
