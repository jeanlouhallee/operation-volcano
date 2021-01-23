package com.project.operationvolcano.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class BookingService implements IBookingService{

    final private ReservationRepository reservationRepository;

    @Autowired
    public BookingService(ReservationRepository reservationRepository){
        this.reservationRepository = reservationRepository;
    }

    public List<LocalDate> checkAvailabilities(Optional<LocalDate> fromDate, Optional<LocalDate> untilDate){

        Stream<LocalDate> availableDates = fromDate.get().datesUntil(untilDate.get());

        return availableDates.collect(Collectors.toList());
    }

    @Override
    public UUID makeReservation(ReservationDto reservation) {

        return reservationRepository.save(new Reservation.ReservationBuilder()
                .checkIn(reservation.getCheckIn())
                .checkOut(reservation.getCheckOut())
                .firstName(reservation.getFirstName())
                .lastName(reservation.getLastName())
                .email(reservation.getEmail()).build()).getReservationId();
    }

    @Override
    public UUID updateReservation(UUID reservationId, ReservationDto reservation) {
        return null;
    }

    @Override
    public void cancelReservation(UUID reservationId) {

    }
}
