package com.project.operationvolcano.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/booking/v1")
public class BookingController {

    final private BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService){
        this.bookingService = bookingService;
    }

    @GetMapping("/availabilities")
    public List<LocalDate> checkAvailabilities(@RequestParam Optional<LocalDate> fromDate, @RequestParam Optional<LocalDate> untilDate){
        log.info("checking availbility between {} and {}.", fromDate, untilDate);

        List<LocalDate> availableDates = this.bookingService.checkAvailabilities(fromDate, untilDate);
        return availableDates;
    }
}
