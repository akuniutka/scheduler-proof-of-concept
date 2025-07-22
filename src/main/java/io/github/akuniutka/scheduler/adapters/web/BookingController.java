package io.github.akuniutka.scheduler.adapters.web;

import io.github.akuniutka.scheduler.adapters.dto.CheckSlotRequest;
import io.github.akuniutka.scheduler.adapters.dto.CheckSlotResponse;
import io.github.akuniutka.scheduler.adapters.exception.BadRequestException;
import io.github.akuniutka.scheduler.application.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/check")
    public CheckSlotResponse checkSlot(@RequestBody @Valid CheckSlotRequest request) {
        return bookingService.checkSlot(request);
    }

    @ExceptionHandler
    public ProblemDetail handleBadRequestException(BadRequestException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}
