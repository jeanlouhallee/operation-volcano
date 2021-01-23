package com.project.operationvolcano.booking;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IBookingService {

    List<LocalDate> checkAvailabilities(Optional<LocalDate> fromDate, Optional<LocalDate> untilDate);

    UUID makeReservation(ReservationDto reservation);

    UUID updateReservation(UUID reservationId, ReservationDto reservation);

    void cancelReservation(UUID reservationId);


}
