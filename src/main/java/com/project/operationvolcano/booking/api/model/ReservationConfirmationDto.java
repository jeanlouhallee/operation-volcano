package com.project.operationvolcano.booking.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationConfirmationDto {

    private UUID ReservationConfirmationNumber;
}
