package com.project.operationvolcano.booking.persistence.model;

import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue
    private UUID reservationId;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String email;

    @Column(unique = true)
    private LocalDate checkIn;

    @Column(unique = true)
    private LocalDate checkOut;
}
