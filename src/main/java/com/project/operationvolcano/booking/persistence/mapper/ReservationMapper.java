package com.project.operationvolcano.booking.persistence.mapper;

import com.project.operationvolcano.booking.api.model.ReservationDto;
import com.project.operationvolcano.booking.persistence.model.Reservation;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mappings({@Mapping(target="checkIn", source="dto.stayDates.fromDate"),
            @Mapping(target="checkOut", source="dto.stayDates.untilDate"),
            @Mapping(target="reservationId", ignore = true)})
    Reservation reservationDTOtoReservation(ReservationDto dto);
}
