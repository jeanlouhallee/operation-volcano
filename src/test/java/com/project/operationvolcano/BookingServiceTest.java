package com.project.operationvolcano;

import com.project.operationvolcano.booking.BookingService;
import com.project.operationvolcano.booking.IBookingService;
import com.project.operationvolcano.booking.persistence.ReservationRepository;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testing class for BookingService implementation")
public class BookingServiceTest {

    private IBookingService subject;

    @Test
    public void test(){
        subject = new BookingService(Mockito.mock(ReservationRepository.class));

        LocalDate baseDate = LocalDate.of(2020, 01, 01);

        List<LocalDate> avaiableDates = subject.checkAvailabilities(baseDate, baseDate.plusDays(10));
        Assert.assertEquals(avaiableDates, baseDate.datesUntil(baseDate.plusDays(10)).collect(Collectors.toList()));
    }
}
