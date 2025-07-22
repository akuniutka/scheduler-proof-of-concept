package io.github.akuniutka.scheduler.application;

import io.github.akuniutka.scheduler.adapters.dto.CheckSlotRequest;
import io.github.akuniutka.scheduler.adapters.dto.CheckSlotResponse;
import io.github.akuniutka.scheduler.adapters.exception.BadRequestException;
import io.github.akuniutka.scheduler.domain.model.Booking;
import io.github.akuniutka.scheduler.domain.model.Event;
import io.github.akuniutka.scheduler.domain.repository.BookingRepository;
import io.github.akuniutka.scheduler.domain.repository.EventRepository;
import org.springframework.stereotype.Service;

@Service
public class BookingServiceImpl implements BookingService {

    private final EventRepository eventRepository;
    private final BookingRepository bookingRepository;

    public BookingServiceImpl(
            EventRepository eventRepository,
            BookingRepository bookingRepository
    ) {
        this.eventRepository = eventRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public CheckSlotResponse checkSlot(CheckSlotRequest request) {
        long startTime = System.currentTimeMillis();
        Event event = eventRepository.findById(request.eventId()).orElseThrow(
                () -> new BadRequestException("Event with id " + request.eventId() + " does not exist")
        );
        if (request.endTime().isBefore(request.startTime()) || request.endTime().equals(request.startTime())) {
            throw new BadRequestException("Slot must end after it starts");
        }
        if (request.endTime().isAfter(event.endTime()) || request.startTime().isBefore(event.startTime())) {
            throw new BadRequestException("Slot must be within event start time and end time");
        }
        Booking intersect = bookingRepository.checkSlot(event.owner(), request.startTime(), request.endTime())
                .orElse(null);
        long endTime = System.currentTimeMillis();
        return new CheckSlotResponse(
                intersect != null,
                intersect,
                endTime - startTime
        );
    }
}
