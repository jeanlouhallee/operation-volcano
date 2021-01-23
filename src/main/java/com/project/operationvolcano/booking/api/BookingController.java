package com.project.operationvolcano.booking.api;

import com.project.operationvolcano.booking.BookingService;
import com.project.operationvolcano.booking.api.model.ReservationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<LocalDate>> checkAvailabilities(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> untilDate){
        log.info("checking availbility between {} and {}.", fromDate, untilDate);

        List<LocalDate> availableDates = bookingService.checkAvailabilities(
                fromDate.orElse(LocalDate.now().plusDays(1)),
                untilDate.orElse(LocalDate.now().plusMonths(1)));

        return new ResponseEntity<>(availableDates, HttpStatus.OK);
    }

    @PostMapping("/reservation")
    public ResponseEntity<UUID> makeReservation(@RequestBody ReservationDto reservation){
        log.info("making a new reservation for {}", reservation.getEmail());

        UUID reservationId = bookingService.makeReservation(reservation);
        return new ResponseEntity<>(reservationId, HttpStatus.CREATED);
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