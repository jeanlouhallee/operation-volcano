package com.project.operationvolcano;

import com.project.operationvolcano.booking.BookingService;
import com.project.operationvolcano.booking.IBookingService;
import com.project.operationvolcano.booking.persistence.ReservationRepository;
import com.project.operationvolcano.booking.persistence.model.Reservation;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testing class for BookingService implementation")
public class BookingServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    private IBookingService subject;

    @Test
    public void givenExistingReservation_whenCheckingAvailabilities_thenReturnAppropriateDates(){

        //given
        subject = new BookingService(reservationRepository);
        LocalDate startDate = LocalDate.of(2021, 1, 1);
        LocalDate endDate = startDate.plusDays(9);
        LocalDate checkIn = LocalDate.of(2021, 1, 3);
        LocalDate checkOut = LocalDate.of(2021, 1, 5);

        //when
        Mockito.when(reservationRepository.findAllReservationsWithinDates(eq(startDate), eq(endDate)))
        .thenReturn(Collections.singletonList(Reservation.builder()
                .checkIn(checkIn)
                .checkOut(checkOut)
                .build()));

        List<LocalDate> avaiableDates = subject.checkAvailabilities(startDate, endDate);

        // expected
        List<LocalDate> expectedDates = startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());
        expectedDates.removeAll(checkIn.datesUntil(checkOut).collect(Collectors.toList()));

        // then
        Assert.assertEquals(expectedDates, avaiableDates);
    }
}
