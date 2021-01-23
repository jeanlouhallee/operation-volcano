package com.project.operationvolcano.booking.exceptions;

public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException() {
        super("Cannot find requested reservation");
    }
}
