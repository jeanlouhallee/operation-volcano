package com.project.operationvolcano.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BookingService {

    @Autowired
    public BookingService(){}

    public List<LocalDate> checkAvailabilities(Optional<LocalDate> fromDate, Optional<LocalDate> untilDate){
        return Collections.singletonList(LocalDate.now());
    }
}
