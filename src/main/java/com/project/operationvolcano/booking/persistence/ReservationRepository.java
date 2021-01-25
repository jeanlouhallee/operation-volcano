package com.project.operationvolcano.booking.persistence;

import com.project.operationvolcano.booking.persistence.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

    //TODO update query
    @Query("select u from Reservation u")
    List<Reservation> findAllReservationsWithinDates(LocalDate checkIn, LocalDate checkOut);

}
