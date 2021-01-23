package com.project.operationvolcano.booking.exceptions;

public class ReservationDatesOutOfRangeException extends RuntimeException {

    public ReservationDatesOutOfRangeException(String errorMessage) {
        super(errorMessage);
    }
}
