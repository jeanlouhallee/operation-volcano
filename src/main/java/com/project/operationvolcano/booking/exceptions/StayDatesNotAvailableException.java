package com.project.operationvolcano.booking.exceptions;

public class StayDatesNotAvailableException extends RuntimeException {
    public StayDatesNotAvailableException() {
        super("These stay dates contain dates that are not available");
    }
}
