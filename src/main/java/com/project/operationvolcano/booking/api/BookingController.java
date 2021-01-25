package com.project.operationvolcano.booking.api;

import com.project.operationvolcano.booking.BookingService;
import com.project.operationvolcano.booking.api.model.DateRangeDto;
import com.project.operationvolcano.booking.api.model.ReservationConfirmationDto;
import com.project.operationvolcano.booking.api.model.ReservationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/booking/v1")
class BookingController {

    private static final LocalDate DEFAULT_INITIAL_DATE = LocalDate.now().plusDays(1);
    private static final LocalDate DEFAULT_MAX_DATE = LocalDate.now().plusMonths(1);

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService){
        this.bookingService = bookingService;
    }

    @GetMapping("/availabilities")
    public ResponseEntity<List<LocalDate>> checkAvailabilities(@RequestBody @Valid Optional<DateRangeDto> rangeOfDatesRequest){
        log.info("checkAvailabilities() {}", rangeOfDatesRequest);

        DateRangeDto rangeOfDates = rangeOfDatesRequest.orElse(new DateRangeDto());

        if(rangeOfDates.getFromDate() == null) rangeOfDates.setFromDate(DEFAULT_INITIAL_DATE);
        if(rangeOfDates.getUntilDate() == null) rangeOfDates.setUntilDate(DEFAULT_MAX_DATE);

        List<LocalDate> availableDates = bookingService.checkAvailabilities(
                rangeOfDates.getFromDate(),
                rangeOfDates.getUntilDate());

        return new ResponseEntity<>(availableDates, HttpStatus.OK);
    }

    @PostMapping("/reservation")
    public ResponseEntity<ReservationConfirmationDto> makeReservation(@RequestBody @Valid ReservationDto reservation){
        log.info("makeReservation() {}", reservation);

        ReservationConfirmationDto reservationConfirmation = bookingService.makeReservation(reservation);
        return new ResponseEntity<>(reservationConfirmation, HttpStatus.CREATED);
    }

    @PutMapping("/reservation/{id}")
    public ResponseEntity<Void> updateReservation(@PathVariable("id") UUID reservationId, @RequestBody ReservationDto reservation){
        log.info("updateReservation() {}", reservationId);

        bookingService.updateReservation(reservationId, reservation);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/reservation/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable("id") UUID reservationId){
        log.info("cancelReservation() {}", reservationId);

        bookingService.cancelReservation(reservationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
