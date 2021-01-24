package com.project.operationvolcano.api.validation;

import com.project.operationvolcano.booking.api.model.ReservationDateRangeDto;
import com.project.operationvolcano.booking.api.validators.ValidReservationDatesValidator;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;

//TODO Must test each case for specific error message.
@ExtendWith(MockitoExtension.class)
@DisplayName("Testing class for ValidDateRangeValidator")
public class ValidReservationDatesValidatorTest {

    ValidReservationDatesValidator subject = new ValidReservationDatesValidator();

    @Mock
    ConstraintValidatorContext context;

    @Test
    public void givenValidDateRangewhenValidating_thenReturnTrue(){
        ReservationDateRangeDto dateRange = new ReservationDateRangeDto(LocalDate.now().plusDays(2), LocalDate.now().plusDays(1));

        Assert.assertTrue(subject.isValid(dateRange, context));
    }

    @Test
    public void givenNoDateRange_whenValidating_thenReturnFalse(){
        mockValidatorContext();
        ReservationDateRangeDto dateRange = new ReservationDateRangeDto(null, null);

        Assert.assertFalse(subject.isValid(dateRange, context));
    }

    @Test
    public void givenExceededNumberOfDays_whenValidating_thenReturnFalse(){
        mockValidatorContext();
        ReservationDateRangeDto dateRange = new ReservationDateRangeDto(LocalDate.now().plusDays(1), LocalDate.now().plusDays(4));

        Assert.assertFalse(subject.isValid(dateRange, context));
    }

    @Test
    public void givenReservationAfter1Month_whenValidating_thenReturnFalse(){
        mockValidatorContext();
        ReservationDateRangeDto dateRange = new ReservationDateRangeDto(
                LocalDate.now().plusMonths(1).minusDays(1),
                LocalDate.now().plusMonths(1).plusDays(1));

        Assert.assertFalse(subject.isValid(dateRange, context));
    }

    private void mockValidatorContext(){
        Mockito.when(context.buildConstraintViolationWithTemplate(any()))
                .thenReturn(Mockito.mock(ConstraintValidatorContext.ConstraintViolationBuilder.class));
    }

}
