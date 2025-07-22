package io.github.akuniutka.scheduler.application;

import io.github.akuniutka.scheduler.domain.model.Booking;
import io.github.akuniutka.scheduler.domain.model.Event;
import io.github.akuniutka.scheduler.domain.model.User;
import io.github.akuniutka.scheduler.domain.repository.BookingRepository;
import io.github.akuniutka.scheduler.domain.repository.EventRepository;
import io.github.akuniutka.scheduler.domain.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Component
public class Runner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(Runner.class);
    private static final Random RANDOM = new Random();

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final BookingRepository bookingRepository;

    public Runner(
            UserRepository userRepository,
            EventRepository eventRepository,
            BookingRepository bookingRepository
    ) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("Creating users...");
        User user1 = new User(1L);
        User user2 = new User(2L);
        userRepository.insert(user1);
        userRepository.insert(user2);
        log.info("Users created");

        log.info("Creating events...");
        Instant startTime = Instant.now().truncatedTo(ChronoUnit.HOURS);
        Instant endTime = startTime.plus(21_000_000, ChronoUnit.HOURS);
        Event event1 = new Event(1L, user1.id(), startTime, endTime);
        Event event2 = new Event(2L, user2.id(), startTime, endTime);
        Event event3 = new Event(3L, user1.id(), startTime, endTime);
        Event event4 = new Event(4L, user2.id(), startTime, endTime);
        eventRepository.insert(event1);
        eventRepository.insert(event2);
        eventRepository.insert(event3);
        eventRepository.insert(event4);
        log.info("Events created");

        log.info("Creating bookings...");
        endTime = startTime;
        Booking[] bookings = new Booking[10_000];
        long bookingCount = 0L;
        for (int i = 0; i < 400; i++) {
            for (int j = 0; j < 10_000; j++) {
                startTime = nextStartTime(endTime);
                endTime = nextEndTime(startTime);
                bookings[j] = new Booking(i * 10_000L + j + 1L, j % 4 + 1L, j % 2 + 1L, startTime, endTime);
            }
            bookingRepository.insertAll(bookings);
            bookingCount = bookingCount + 10_000L;
            log.info("{} bookings created", bookingCount);

        }
        log.info("Bookings created");
    }

    private Instant nextStartTime(Instant endTime) {
        return endTime.plus(RANDOM.nextInt(1, 5), ChronoUnit.HOURS);
    }

    private Instant nextEndTime(Instant startTime) {
        return startTime.plus(RANDOM.nextInt(1, 5) * 15L, ChronoUnit.MINUTES);
    }
}
