package io.github.akuniutka.scheduler.application.service.impl;

import io.github.akuniutka.scheduler.application.service.BookingService;
import io.github.akuniutka.scheduler.domain.model.Booking;
import io.github.akuniutka.scheduler.domain.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository repository;

    public BookingServiceImpl(BookingRepository repository) {
        this.repository = repository;
    }

    @Override
    public void insertAll(Collection<Booking> bookings) {
        repository.insertAll(bookings);
    }

    @Override
    public Optional<Booking> findAnyByOwnerIdBetweenStartTimeAndEndTime(long ownerId, Instant startTime, Instant endTime) {
        return repository.findAnyByOwnerIdBetweenStartTimeAndEndTime(ownerId, startTime, endTime);
    }

    @Override
    public List<Booking> findAllByOwnerIdOrderByStartTime(long ownerId) {
        return repository.findAllByOwnerIdOrderByStartTime(ownerId);
    }
}
