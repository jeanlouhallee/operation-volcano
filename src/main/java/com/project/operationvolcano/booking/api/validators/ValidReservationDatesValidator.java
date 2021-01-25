package com.project.operationvolcano.booking.api.validators;

import com.project.operationvolcano.booking.api.model.ReservationDateRangeDto;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

@Slf4j
public class ValidReservationDatesValidator implements ConstraintValidator<ValidReservationDatesConstraint, ReservationDateRangeDto> {

    private static final int MAX_NUMBER_OF_DAYS = 3;
    private static final int MAX_NUMBER_OF_MONTHS = 1;

    @Override
    public void initialize(ValidReservationDatesConstraint constraintAnnotation) { }

    @Override
    public boolean isValid(ReservationDateRangeDto value, ConstraintValidatorContext context) {
        log.debug("Validating {}", value);

        boolean isValid = true;
        LocalDate startDate = value.getFromDate();
        LocalDate endDate = value.getUntilDate();
        context.disableDefaultConstraintViolation();

        if(startDate == null || endDate == null) {
            context.buildConstraintViolationWithTemplate("") //Already treated in ValidDateRangeValidator
                    .addConstraintViolation();

            isValid = false;
        } else if(DAYS.between(startDate, endDate) > MAX_NUMBER_OF_DAYS) {
            context.buildConstraintViolationWithTemplate(String.format("You cannot exceed %d days for a stay", MAX_NUMBER_OF_DAYS))
                    .addConstraintViolation();

            isValid = false;
        } else if(endDate.isAfter(LocalDate.now().plusMonths(1))) {
            context.buildConstraintViolationWithTemplate(String.format("You cannot reserve a stay more than %d month ahead", MAX_NUMBER_OF_MONTHS))
                    .addConstraintViolation();

            isValid = false;
        }

        log.debug("Validation returns {}", isValid);
        return isValid;
    }
}
