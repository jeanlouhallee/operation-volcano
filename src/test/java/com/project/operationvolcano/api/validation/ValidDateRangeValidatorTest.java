package com.project.operationvolcano.api.validation;

import com.project.operationvolcano.booking.api.model.DateRangeDto;
import com.project.operationvolcano.booking.api.validators.ValidDateRangeValidator;
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
public class ValidDateRangeValidatorTest {

    ValidDateRangeValidator subject = new ValidDateRangeValidator();

    @Mock
    ConstraintValidatorContext context;

    @Test
    public void givenValidDateRange_whenValidating_thenReturnTrue(){
        DateRangeDto dateRange = new DateRangeDto(LocalDate.now().plusDays(1), LocalDate.now().plusDays(10));
        Assert.assertTrue(subject.isValid(dateRange, context));
    }

    @Test
    public void givenNoDateRange_whenValidating_thenReturnTrue(){
        DateRangeDto dateRange = new DateRangeDto(null, null);
        Assert.assertTrue(subject.isValid(dateRange, context));
    }

    @Test
    public void givenEndDateBeforeStartDate_whenValidating_thenReturnFalse(){
        mockValidatorContext();
        DateRangeDto dateRange = new DateRangeDto(LocalDate.now().plusDays(2), LocalDate.now().plusDays(1));

        Assert.assertFalse(subject.isValid(dateRange, context));
    }

    @Test
    public void givenStartDateOnly_whenValidating_thenReturnFalse(){
        mockValidatorContext();
        DateRangeDto dateRange = new DateRangeDto(LocalDate.now().plusDays(1), null);

        Assert.assertFalse(subject.isValid(dateRange, context));
    }

    @Test
    public void givenEndDateOnly_whenValidating_thenReturnFalse(){
        mockValidatorContext();
        DateRangeDto dateRange = new DateRangeDto(null, LocalDate.now().plusDays(10));

        Assert.assertFalse(subject.isValid(dateRange, context));

    }

    @Test
    public void givenStartDateToday_whenValidating_thenReturnFalse(){
        mockValidatorContext();
        DateRangeDto dateRange = new DateRangeDto(LocalDate.now(), LocalDate.now().plusDays(10));

        Assert.assertFalse(subject.isValid(dateRange, context));
    }

    @Test
    public void givenStartInThePast_whenValidating_thenReturnFalse(){
        mockValidatorContext();
        DateRangeDto dateRange = new DateRangeDto(LocalDate.now().minusDays(1), LocalDate.now());

        Assert.assertFalse(subject.isValid(dateRange, context));
    }

    private void mockValidatorContext(){
        Mockito.when(context.buildConstraintViolationWithTemplate(any()))
                .thenReturn(Mockito.mock(ConstraintValidatorContext.ConstraintViolationBuilder.class));
    }
}
