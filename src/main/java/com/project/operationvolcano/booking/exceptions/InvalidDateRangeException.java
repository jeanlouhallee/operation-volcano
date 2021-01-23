package com.project.operationvolcano.booking.exceptions;

public class InvalidDateRangeException extends RuntimeException {
    public InvalidDateRangeException(String errorMessage){
        super(errorMessage);
    }
}
