package com.project.operationvolcano.booking;

import com.project.operationvolcano.booking.api.model.ReservationConfirmationDto;
import com.project.operationvolcano.booking.api.model.ReservationDto;
import com.project.operationvolcano.booking.persistence.ReservationRepository;
import com.project.operationvolcano.booking.persistence.model.Reservation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class BookingService implements IBookingService {

    private final ReservationRepository reservationRepository;

    @Autowired
    public BookingService(ReservationRepository reservationRepository){
        this.reservationRepository = reservationRepository;
    }

    public List<LocalDate> checkAvailabilities(LocalDate fromDate, LocalDate untilDate){

        Stream<LocalDate> availableDates = fromDate.datesUntil(untilDate);
        return availableDates.collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ReservationConfirmationDto makeReservation(ReservationDto reservation) {

        UUID reservationId = reservationRepository.save(Reservation.builder()
                .checkIn(reservation.getCheckIn())
                .checkOut(reservation.getCheckOut())
                .firstName(reservation.getFirstName())
                .lastName(reservation.getLastName())
                .email(reservation.getEmail()).build()).getReservationId();

        return new ReservationConfirmationDto(reservationId);
    }

    @Transactional
    @Override
    public void updateReservation(UUID reservationId, ReservationDto updatedReservation) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(RuntimeException::new);

        reservation.setFirstName(updatedReservation.getFirstName());
        reservation.setLastName(updatedReservation.getLastName());
        reservation.setEmail(updatedReservation.getEmail());
        reservation.setCheckIn(updatedReservation.getCheckIn());
        reservation.setCheckOut(updatedReservation.getCheckOut());
    }

    @Override
    public void cancelReservation(UUID reservationId) {
        this.reservationRepository.deleteById(reservationId);
    }
}
