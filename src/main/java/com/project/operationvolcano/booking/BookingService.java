package com.project.operationvolcano.booking;

import com.project.operationvolcano.booking.api.model.ReservationConfirmationDto;
import com.project.operationvolcano.booking.api.model.ReservationDto;
import com.project.operationvolcano.booking.exceptions.InvalidDateRangeException;
import com.project.operationvolcano.booking.exceptions.ReservationNotFoundException;
import com.project.operationvolcano.booking.persistence.ReservationRepository;
import com.project.operationvolcano.booking.persistence.model.Reservation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookingService implements IBookingService {

    private final ReservationRepository reservationRepository;

    @Autowired
    public BookingService(ReservationRepository reservationRepository){
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    @Override
    public List<LocalDate> checkAvailabilities(LocalDate fromDate, LocalDate untilDate){
        log.info("checkAvailabilities() {}, {}", fromDate, untilDate);

        if(fromDate == null || untilDate == null) {
            throw new InvalidDateRangeException("");
        }
        List<LocalDate> availableDates = fromDate.datesUntil(untilDate.plusDays(1)).collect(Collectors.toList());
        List<Reservation> reservations = reservationRepository.findAllReservationsWithinDates(fromDate, untilDate);

        reservations.forEach( r -> availableDates.removeAll(
                r.getCheckIn().datesUntil(r.getCheckOut()).collect(Collectors.toList()))
        );

        return availableDates;
    }

    @Transactional
    @Override
    public ReservationConfirmationDto makeReservation(ReservationDto reservation) {
        log.info("makeReservation() {}", reservation);

        UUID reservationId = reservationRepository.save(Reservation.builder()
                .checkIn(reservation.getStayDates().getFromDate())
                .checkOut(reservation.getStayDates().getUntilDate())
                .firstName(reservation.getFirstName())
                .lastName(reservation.getLastName())
                .email(reservation.getEmail()).build()).getReservationId();

        return new ReservationConfirmationDto(reservationId);
    }

    @Transactional
    @Override
    public void updateReservation(UUID reservationId, ReservationDto updatedReservation) {
        log.info("updateReservation() {}", updatedReservation);

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(ReservationNotFoundException::new);

        reservation.setFirstName(updatedReservation.getFirstName());
        reservation.setLastName(updatedReservation.getLastName());
        reservation.setEmail(updatedReservation.getEmail());
        reservation.setCheckIn(updatedReservation.getStayDates().getFromDate());
        reservation.setCheckOut(updatedReservation.getStayDates().getUntilDate());
    }

    @Transactional
    @Override
    public void cancelReservation(UUID reservationId) {
        log.info("cancelReservation() {}", reservationId);
        try {
            this.reservationRepository.deleteById(reservationId);
        } catch (EmptyResultDataAccessException ex) {
            throw new ReservationNotFoundException();
        }
    }
}
