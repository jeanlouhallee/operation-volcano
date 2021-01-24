package com.project.operationvolcano.booking.api.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ReservationDto {

    private String firstName;

    private String lastName;

    private String email;

    private LocalDate checkIn;

    private LocalDate checkOut;
}
