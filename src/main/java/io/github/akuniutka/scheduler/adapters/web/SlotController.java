package io.github.akuniutka.scheduler.adapters.web;

import io.github.akuniutka.scheduler.adapters.dto.CheckSlotRequest;
import io.github.akuniutka.scheduler.adapters.dto.CheckSlotResponse;
import io.github.akuniutka.scheduler.adapters.dto.FreeSlotsResponse;
import io.github.akuniutka.scheduler.adapters.exception.BadRequestException;
import io.github.akuniutka.scheduler.adapters.exception.NotFoundException;
import io.github.akuniutka.scheduler.adapters.exception.UnprocessableEntityException;
import io.github.akuniutka.scheduler.application.usecase.CheckSlotUseCase;
import io.github.akuniutka.scheduler.application.usecase.FindFreeSlotsUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/slots")
public class SlotController {

    private final FindFreeSlotsUseCase findFreeSlotsUseCase;
    private final CheckSlotUseCase checkSlotUseCase;

    public SlotController(
            FindFreeSlotsUseCase findFreeSlotsUseCase,
            CheckSlotUseCase checkSlotUseCase
    ) {
        this.findFreeSlotsUseCase = findFreeSlotsUseCase;
        this.checkSlotUseCase = checkSlotUseCase;
    }

    @GetMapping
    public FreeSlotsResponse findFreeSlots(@RequestParam long eventId) {
        return findFreeSlotsUseCase.findFreeSlots(eventId);
    }

    @PostMapping("/check")
    public CheckSlotResponse checkSlot(@RequestBody @Valid CheckSlotRequest request) {
        return checkSlotUseCase.checkSlot(request);
    }

    @ExceptionHandler
    public ProblemDetail handleBadRequestException(BadRequestException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler
    public ProblemDetail handleNotFoundException(NotFoundException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler
    public ProblemDetail handleUnprocessableEntityException(UnprocessableEntityException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
    }
}
