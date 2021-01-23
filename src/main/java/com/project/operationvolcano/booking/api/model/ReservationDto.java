package com.project.operationvolcano.booking.api.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationDto {

    private String firstName;

    private String lastName;

    private String email;

    private LocalDate checkIn;

    private LocalDate checkOut;
}