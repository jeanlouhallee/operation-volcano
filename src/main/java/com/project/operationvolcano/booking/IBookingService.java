package com.project.operationvolcano.booking;

import com.project.operationvolcano.booking.api.model.ReservationConfirmationDto;
import com.project.operationvolcano.booking.api.model.ReservationDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IBookingService {

    List<LocalDate> checkAvailabilities(LocalDate fromDate, LocalDate untilDate);

    ReservationConfirmationDto makeReservation(ReservationDto reservation);

    void updateReservation(UUID reservationId, ReservationDto reservation);

    void cancelReservation(UUID reservationId);


}
