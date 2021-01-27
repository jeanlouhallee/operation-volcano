package com.project.operationvolcano.booking.api.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Valid
@Data
@Builder
public class ReservationDto {

    @NotEmpty(message = "firstName cannot be empty")
    private String firstName;

    @NotEmpty(message = "lastName cannot be empty")
    private String lastName;

    @NotEmpty(message = "email cannot be empty")
    private String email;

    @Valid
    @NotNull
    ReservationDateRangeDto stayDates;
}
