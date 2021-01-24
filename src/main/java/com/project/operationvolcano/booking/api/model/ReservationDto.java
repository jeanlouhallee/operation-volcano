package com.project.operationvolcano.booking.api.model;

import com.project.operationvolcano.booking.api.validators.ValidReservationDatesConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Valid
@Data
@Builder
public class ReservationDto {

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    private String email;

    @Valid
    @NotNull
    ReservationDateRangeDto stayDates;
}
