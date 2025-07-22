package io.github.akuniutka.scheduler.application;

import io.github.akuniutka.scheduler.adapters.dto.CheckSlotRequest;
import io.github.akuniutka.scheduler.adapters.dto.CheckSlotResponse;

public interface BookingService {

    CheckSlotResponse checkSlot(CheckSlotRequest request);
}
