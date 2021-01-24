package com.project.operationvolcano.booking.api.validators;

import com.project.operationvolcano.booking.api.model.DateRangeDto;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

@Slf4j
public class ValidDateRangeValidator implements ConstraintValidator <ValidDateRangeConstraint, DateRangeDto> {

    @Override
    public void initialize(ValidDateRangeConstraint constraintAnnotation) { }

    @Override
    public boolean isValid(DateRangeDto value, ConstraintValidatorContext context) {
        log.debug("Validating {}", value);

        boolean isValid = true;
        LocalDate startDate = value.getFromDate();
        LocalDate endDate = value.getUntilDate();
        context.disableDefaultConstraintViolation();

        if(startDate == null ^ endDate == null) {
            context.buildConstraintViolationWithTemplate("You must specify the starting date with end date")
            .addConstraintViolation();

            isValid = false;
        } else if(startDate != null) {

            if(!startDate.isBefore(endDate)) {
                context.buildConstraintViolationWithTemplate("Start date must be before end date.")
                        .addConstraintViolation();

                isValid = false;
            }
            if(startDate.isBefore(LocalDate.now().plusDays(1))) {
                context.buildConstraintViolationWithTemplate("Start date must be after today.")
                        .addConstraintViolation();

                isValid = false;
            }

        }

        log.debug("Validation returns {}", isValid);
        return isValid;
    }
}
