package io.github.akuniutka.scheduler.application.usecase;

import io.github.akuniutka.scheduler.adapters.dto.CheckSlotRequest;
import io.github.akuniutka.scheduler.adapters.dto.CheckSlotResponse;
import io.github.akuniutka.scheduler.adapters.exception.BadRequestException;
import io.github.akuniutka.scheduler.adapters.exception.UnprocessableEntityException;
import io.github.akuniutka.scheduler.application.service.BookingService;
import io.github.akuniutka.scheduler.application.service.EventService;
import io.github.akuniutka.scheduler.domain.model.Booking;
import io.github.akuniutka.scheduler.domain.model.Event;
import org.springframework.stereotype.Service;

@Service
public class CheckSlotUseCase {

    private final EventService eventService;
    private final BookingService bookingService;

    public CheckSlotUseCase(EventService eventService, BookingService bookingService) {
        this.eventService = eventService;
        this.bookingService = bookingService;
    }

    public CheckSlotResponse checkSlot(CheckSlotRequest request) {
        long startTime = System.currentTimeMillis();
        if (request.endTime().isBefore(request.startTime()) || request.endTime().equals(request.startTime())) {
            throw new BadRequestException("Slot must end after it starts");
        }
        Event event = eventService.getById(request.eventId());
        if (request.endTime().isAfter(event.endTime()) || request.startTime().isBefore(event.startTime())) {
            throw new UnprocessableEntityException("Slot must be within event start time and end time");
        }
        Booking intersection = bookingService.findAnyByOwnerIdBetweenStartTimeAndEndTime(event.owner(),
                request.startTime(), request.endTime()).orElse(null);
        long endTime = System.currentTimeMillis();
        return new CheckSlotResponse(
                intersection != null,
                intersection,
                endTime - startTime
        );
    }
}
