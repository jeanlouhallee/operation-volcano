package com.project.operationvolcano.booking.exceptions;

public class ReservationDaysExceededException extends RuntimeException {

    public ReservationDaysExceededException(String errorMessage) {
        super(errorMessage);
    }
}
