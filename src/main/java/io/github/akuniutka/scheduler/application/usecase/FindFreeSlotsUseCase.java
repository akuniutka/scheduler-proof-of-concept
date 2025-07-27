package io.github.akuniutka.scheduler.application.usecase;

import io.github.akuniutka.scheduler.adapters.dto.FreeSlotsResponse;
import io.github.akuniutka.scheduler.application.service.BookingService;
import io.github.akuniutka.scheduler.application.service.EventService;
import io.github.akuniutka.scheduler.application.service.SlotService;
import io.github.akuniutka.scheduler.domain.model.Booking;
import io.github.akuniutka.scheduler.domain.model.Event;
import io.github.akuniutka.scheduler.domain.model.Slot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FindFreeSlotsUseCase {

    private static final Logger log = LoggerFactory.getLogger(FindFreeSlotsUseCase.class);

    private final EventService eventService;
    private final BookingService bookingService;
    private final SlotService slotService;

    public FindFreeSlotsUseCase(EventService eventService, BookingService bookingService, SlotService slotService) {
        this.eventService = eventService;
        this.bookingService = bookingService;
        this.slotService = slotService;
    }

    public FreeSlotsResponse findFreeSlots(long eventId) {
        log.info("Finding free slots for event {}", eventId);
        long startTime = System.currentTimeMillis();
        Event event = eventService.getById(eventId);
        log.info("Found event {}", event);
        List<Slot> slots = slotService.findSlotsByEventIdOrderByStartTime(eventId);
        log.info("Slots loaded {}", slots.size());
        if (slots.isEmpty()) {
            long endTime = System.currentTimeMillis();
            return toResponse(List.of(), endTime - startTime, 0L);
        }
        List<Booking> bookings = bookingService.findAllByOwnerIdOrderByStartTime(event.owner());
        log.info("Bookings loaded {}", bookings.size());
        if (bookings.isEmpty()) {
            long endTime = System.currentTimeMillis();
            return toResponse(slots, endTime - startTime, 0L);
        }
        long midTime = System.currentTimeMillis();
        log.info("Time to load data: {}", midTime - startTime);
        List<Slot> freeSlots = skipSlotsWithIntersections(slots, bookings);
        long endTime = System.currentTimeMillis();
        return toResponse(freeSlots, midTime - startTime, endTime - midTime);
    }

    private List<Slot> skipSlotsWithIntersections(List<Slot> slots, List<Booking> bookings) {
        List<Slot> freeSlots = new ArrayList<>(slots.size());
        int slotIndex = 0;
        int bookingIndex = 0;
        while (slotIndex < slots.size() && bookingIndex < bookings.size()) {
            Booking booking = bookings.get(bookingIndex);
            Slot slot = slots.get(slotIndex);
            if (!booking.endTime().isAfter(slot.startTime())) {
                bookingIndex++;
            } else {
                if (!slot.endTime().isAfter(booking.startTime())) {
                    freeSlots.add(slot);
                }
                slotIndex++;
            }
        }
        while (slotIndex < slots.size()) {
            Slot slot = slots.get(slotIndex);
            freeSlots.add(slot);
            slotIndex++;
        }
        return freeSlots;
    }

    private FreeSlotsResponse toResponse(List<Slot> slots, long timeToLoadData, long timeToCalculate) {
        List<Slot> firstPage = slots.stream().limit(10).toList();
        return new FreeSlotsResponse(slots.size(), firstPage, timeToLoadData, timeToCalculate, timeToLoadData
                + timeToCalculate);
    }
}
