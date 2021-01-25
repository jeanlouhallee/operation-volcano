package com.project.operationvolcano.booking;

import com.project.operationvolcano.booking.api.model.ReservationConfirmationDto;
import com.project.operationvolcano.booking.api.model.ReservationDto;
import com.project.operationvolcano.booking.exceptions.ReservationNotFoundException;
import com.project.operationvolcano.booking.exceptions.StayDatesNotAvailableException;
import com.project.operationvolcano.booking.persistence.ReservationRepository;
import com.project.operationvolcano.booking.persistence.mapper.ReservationMapper;
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

// TODO create mapper for DTO to Entity
@Slf4j
@Service
public class BookingService implements IBookingService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;

    @Autowired
    public BookingService(ReservationRepository reservationRepository, ReservationMapper reservationMapper){
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
    }

    @Transactional
    @Override
    public List<LocalDate> checkAvailabilities(LocalDate fromDate, LocalDate untilDate){
        log.info("checkAvailabilities() {}, {}", fromDate, untilDate);

        return getAvailableDates(fromDate, untilDate);
    }

    @Transactional
    @Override
    public ReservationConfirmationDto makeReservation(ReservationDto reservationDto) {
        log.info("makeReservation() {}", reservationDto);
        LocalDate fromDate = reservationDto.getStayDates().getFromDate();
        LocalDate untilDate = reservationDto.getStayDates().getUntilDate();

        if(datesAreNotAvailable(fromDate, untilDate)) {
            throw new StayDatesNotAvailableException();
        }

        Reservation reservation = reservationMapper.reservationDTOtoReservation(reservationDto);

        UUID reservationId = reservationRepository.save(reservation).getReservationId();

        return new ReservationConfirmationDto(reservationId);
    }

    @Transactional
    @Override
    public void updateReservation(UUID reservationId, ReservationDto reservation) {
        log.info("updateReservation() {}", reservation);
        LocalDate fromDate = reservation.getStayDates().getFromDate();
        LocalDate untilDate = reservation.getStayDates().getUntilDate();

        // all available dates (which includes this reservation stay dates)
        List<LocalDate> availableDates = getAvailableDates(fromDate, untilDate);

        Reservation existingReservation = reservationRepository.findById(reservationId)
                .orElseThrow(ReservationNotFoundException::new);

        // must put the dates of this reservations because the user has choice to keep them
        availableDates.addAll(
                existingReservation.getCheckIn().datesUntil(existingReservation.getCheckOut())
                        .collect(Collectors.toList()));

        if(datesAreNotAvailable(availableDates, fromDate, untilDate)) {
            throw new StayDatesNotAvailableException();
        }

        Reservation updatableReservation = reservationMapper.reservationDTOtoReservation(reservation);
        updatableReservation.setReservationId(existingReservation.getReservationId());

        reservationRepository.save(updatableReservation);
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

    private List<LocalDate> getAvailableDates(LocalDate fromDate, LocalDate untilDate) {
        List<LocalDate> availableDates = fromDate.datesUntil(untilDate.plusDays(1)).collect(Collectors.toList());
        List<Reservation> reservations = reservationRepository.findAllReservationsWithinDates(fromDate, untilDate);

        reservations.forEach( r -> availableDates.removeAll(
                r.getCheckIn().datesUntil(r.getCheckOut()).collect(Collectors.toList()))
        );
        return availableDates;
    }

    private boolean datesAreNotAvailable(LocalDate fromDate, LocalDate untilDate){
        return !getAvailableDates(fromDate, untilDate).containsAll(fromDate.datesUntil(untilDate).collect(Collectors.toList()));
    }

    private boolean datesAreNotAvailable(List<LocalDate> rangeOfDates, LocalDate fromDate, LocalDate untilDate) {
        return !rangeOfDates.containsAll(fromDate.datesUntil(untilDate).collect(Collectors.toList()));
    }
}
