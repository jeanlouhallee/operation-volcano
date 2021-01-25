package com.project.operationvolcano.booking.api.model;

import com.project.operationvolcano.booking.api.validators.ValidReservationDatesConstraint;

import java.time.LocalDate;

@ValidReservationDatesConstraint
public class ReservationDateRangeDto extends DateRangeDto {

    public ReservationDateRangeDto(LocalDate fromDate, LocalDate untilDate) {
        super(fromDate, untilDate);
    }
}
