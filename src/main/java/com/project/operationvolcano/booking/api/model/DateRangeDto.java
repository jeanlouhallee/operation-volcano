package com.project.operationvolcano.booking.api.model;

import com.project.operationvolcano.booking.api.validators.ValidDateRangeConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@ValidDateRangeConstraint
@Data
@AllArgsConstructor
public class DateRangeDto {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fromDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate untilDate;
}
