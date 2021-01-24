package com.project.operationvolcano.booking.api;

import com.project.operationvolcano.booking.BookingService;
import com.project.operationvolcano.booking.api.model.DateRangeDto;
import com.project.operationvolcano.booking.api.model.ReservationConfirmationDto;
import com.project.operationvolcano.booking.api.model.ReservationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService){
        this.bookingService = bookingService;
    }

    @GetMapping("/availabilities")
    public ResponseEntity<List<LocalDate>> checkAvailabilities(@RequestBody @Valid Optional<DateRangeDto> rangeOfDatesRequest){
        log.info("checking availbility between for {}", rangeOfDatesRequest);

        DateRangeDto rangeOfDates = rangeOfDatesRequest.orElse(
                new DateRangeDto(
                        LocalDate.now().plusDays(1),
                        LocalDate.now().plusMonths(1)));

        List<LocalDate> availableDates = bookingService.checkAvailabilities(
                rangeOfDates.getFromDate(),
                rangeOfDates.getUntilDate());

        return new ResponseEntity<>(availableDates, HttpStatus.OK);
    }

    @PostMapping("/reservation")
    public ResponseEntity<ReservationConfirmationDto> makeReservation(@RequestBody @Valid ReservationDto reservation){
        log.info("making a new reservation for {}", reservation.getEmail());

        ReservationConfirmationDto reservationConfirmation = bookingService.makeReservation(reservation);
        return new ResponseEntity<>(reservationConfirmation, HttpStatus.CREATED);
    }

    @PutMapping("/reservation/{id}")
    public ResponseEntity<Void> updateReservation(@PathVariable("id") UUID reservationId, @RequestBody ReservationDto reservation){
        bookingService.updateReservation(reservationId, reservation);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/reservation/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable("id") UUID reservationId){
        bookingService.cancelReservation(reservationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
