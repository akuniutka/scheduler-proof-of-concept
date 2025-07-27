package io.github.akuniutka.scheduler.application.usecase;

import io.github.akuniutka.scheduler.application.service.BookingService;
import io.github.akuniutka.scheduler.application.service.EventService;
import io.github.akuniutka.scheduler.application.service.SlotService;
import io.github.akuniutka.scheduler.application.service.UserService;
import io.github.akuniutka.scheduler.domain.model.Booking;
import io.github.akuniutka.scheduler.domain.model.Event;
import io.github.akuniutka.scheduler.domain.model.Slot;
import io.github.akuniutka.scheduler.domain.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class Runner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(Runner.class);
    private static final Random RANDOM = new Random();

    private final UserService userService;
    private final EventService eventService;
    private final BookingService bookingService;
    private final SlotService slotService;

    public Runner(
            UserService userService,
            EventService eventService,
            BookingService bookingService,
            SlotService slotService
    ) {
        this.userService = userService;
        this.eventService = eventService;
        this.bookingService = bookingService;
        this.slotService = slotService;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("Creating users...");
        User user1 = new User(1L);
        User user2 = new User(2L);
        userService.insert(user1);
        userService.insert(user2);
        log.info("Users created");

        log.info("Creating events...");
        Instant startTime = Instant.now().truncatedTo(ChronoUnit.HOURS);
        Instant endTime = startTime.plus(21_000_000, ChronoUnit.HOURS);
        Event event1 = new Event(1L, user1.id(), startTime, endTime);
        Event event2 = new Event(2L, user2.id(), startTime, endTime);
        Event event3 = new Event(3L, user1.id(), startTime, endTime);
        Event event4 = new Event(4L, user2.id(), startTime, endTime);
        eventService.insert(event1);
        eventService.insert(event2);
        eventService.insert(event3);
        eventService.insert(event4);
        log.info("Events created");

        log.info("Creating bookings and slots...");
        endTime = startTime;
        long bookingCount = 0L;
        long slotsCount = 0L;
        for (int i = 0; i < 400; i++) {
            List<Booking> bookings = new ArrayList<>(10_000);
            List<Slot> slots = new ArrayList<>(10_000);
            for (int j = 0; j < 10_000; j++) {
                startTime = nextStartTime(endTime);
                endTime = nextEndTime(startTime);
                Booking booking = new Booking(i * 10_000L + j + 1L, j % 4 + 1L, j % 2 + 1L, startTime, endTime);
                bookings.add(booking);
                Slot slot = new Slot(i * 10_000L + j + 1L, 1L, startTime, endTime);
                slots.add(slot);
            }
            bookingService.insertAll(bookings);
            bookingCount = bookingCount + bookings.size();
            log.info("{} bookings created", bookingCount);
            slotService.insertAll(slots);
            slotsCount = slotsCount + slots.size();
            log.info("{} slots created", slotsCount);
        }
        log.info("Bookings and slots created");
    }

    private Instant nextStartTime(Instant endTime) {
        return endTime.plus(RANDOM.nextInt(1, 5), ChronoUnit.HOURS);
    }

    private Instant nextEndTime(Instant startTime) {
        return startTime.plus(RANDOM.nextInt(1, 5) * 15L, ChronoUnit.MINUTES);
    }
}
