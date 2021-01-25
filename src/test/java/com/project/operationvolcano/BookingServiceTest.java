package com.project.operationvolcano;

import com.project.operationvolcano.booking.BookingService;
import com.project.operationvolcano.booking.IBookingService;
import com.project.operationvolcano.booking.api.model.ReservationDateRangeDto;
import com.project.operationvolcano.booking.api.model.ReservationDto;
import com.project.operationvolcano.booking.exceptions.StayDatesNotAvailableException;
import com.project.operationvolcano.booking.persistence.ReservationRepository;
import com.project.operationvolcano.booking.persistence.mapper.ReservationMapper;
import com.project.operationvolcano.booking.persistence.model.Reservation;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testing class for BookingService implementation")
public class BookingServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    private ReservationMapper reservationMapper = Mappers.getMapper(ReservationMapper.class);

    private IBookingService subject;

    @Test
    public void givenExistingReservation_whenCheckingAvailabilities_thenReturnAppropriateDates(){

        //given
        subject = new BookingService(reservationRepository, reservationMapper);
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

    @Test
    public void givenExistingReservations_whenAddingOverlappingReservation_thenThrowException(){

        //given
        subject = new BookingService(reservationRepository, reservationMapper);
        LocalDate existingCheckIn = LocalDate.of(2021, 1, 3);
        LocalDate existingCheckout = LocalDate.of(2021, 1, 5);
        LocalDate newCheckIn = LocalDate.of(2021, 1, 2);
        LocalDate newCheckOut = LocalDate.of(2021, 1, 4);

        //when
        Mockito.when(reservationRepository.findAllReservationsWithinDates(any(), any()))
                .thenReturn(Collections.singletonList(Reservation.builder()
                        .checkIn(existingCheckIn)
                        .checkOut(existingCheckout)
                        .build()));

        Assertions.assertThrows(StayDatesNotAvailableException.class, () -> {
            subject.makeReservation(ReservationDto.builder()
                    .stayDates(new ReservationDateRangeDto(newCheckIn, newCheckOut))
                    .build());
        });
    }


    @Test
    public void givenExistingReservations_whenUpdatingOverlappingReservation_thenThrowException(){

        //given
        subject = new BookingService(reservationRepository, reservationMapper);
        LocalDate notMyCheckIn = LocalDate.of(2021, 1, 3);
        LocalDate notMyCheckout = LocalDate.of(2021, 1, 5);
        LocalDate myCheckIn = LocalDate.of(2021, 1, 1);
        LocalDate myCheckOut = LocalDate.of(2021, 1, 3);
        LocalDate newCheckIn = LocalDate.of(2021, 1, 2);
        LocalDate newCheckOut = LocalDate.of(2021, 1, 4);


        //when
        Mockito.when(reservationRepository.findAllReservationsWithinDates(any(), any()))
                .thenReturn(Collections.singletonList(Reservation.builder()
                        .checkIn(notMyCheckIn)
                        .checkOut(notMyCheckout)
                        .build()));

        //when
        Mockito.when(reservationRepository.findById(any()))
                .thenReturn(Optional.of(Reservation.builder()
                        .checkIn(myCheckIn)
                        .checkOut(myCheckOut)
                        .build()));

        Assertions.assertThrows(StayDatesNotAvailableException.class, () -> {
            subject.updateReservation(UUID.randomUUID(),
                    ReservationDto.builder()
                    .stayDates(new ReservationDateRangeDto(newCheckIn, newCheckOut))
                    .build());
        });
    }
}
