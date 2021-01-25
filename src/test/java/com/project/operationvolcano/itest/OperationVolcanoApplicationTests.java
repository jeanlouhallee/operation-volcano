package com.project.operationvolcano.itest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.operationvolcano.booking.IBookingService;
import com.project.operationvolcano.booking.api.model.DateRangeDto;
import com.project.operationvolcano.booking.api.model.ReservationConfirmationDto;
import com.project.operationvolcano.booking.api.model.ReservationDateRangeDto;
import com.project.operationvolcano.booking.api.model.ReservationDto;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@DisplayName("Operaction volcano integration tests")
class OperationVolcanoApplicationTests {

	private static final String APPLICATION_JSON = "application/json";
	private static final String BOOKING_API = "/booking/v1";

	@ClassRule
	public static PostgreSQLContainer postgreSQLContainer = PostgresTestContainer.getInstance();

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	IBookingService bookingService;

	@Test
	void contextLoads() {
	}

	@Test
	void givenStartDateBeforeEndDate_whengettingAvailabilities_thenValidatorsAreTriggers() throws Exception {

		//given
		DateRangeDto requestBody = new DateRangeDto(LocalDate.now().plusDays(11), LocalDate.now().plusDays(10));


		//when
		mockMvc.perform(get(BOOKING_API + "/availabilities")
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestBody)))

		//then
				.andExpect(status().is4xxClientError())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message")
						.value("[Start date must be before end date]"));
	}

	@Test
	void givenNewReservation_whengDeletingThisReservation_ReservationExistsAndreturns204() throws Exception {

		//given
		ReservationDto requestBody = ReservationDto.builder()
										.firstName("John")
										.lastName("Smith")
										.email("js@bloop.com")
										.stayDates(new ReservationDateRangeDto(LocalDate.now().plusDays(1), LocalDate.now().plusDays(3)))
										.build();

		MvcResult result = mockMvc.perform(post(BOOKING_API + "/reservation")
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestBody)))
				.andExpect(status().isCreated())
				.andReturn();

		ReservationConfirmationDto response = objectMapper.readValue(result.getResponse().getContentAsString(), ReservationConfirmationDto.class);

		//when
		mockMvc.perform(delete(BOOKING_API + "/reservation/" + response.getReservationConfirmationNumber())
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestBody)))

		//then
				.andExpect(status().isNoContent());
	}

	@Test
	public void givenExistingReservations_whenCheckingAvailabilities_thenReturnAppropriateDates() {

		//given reservations in db/inserts/V1.0.1__tests_inserts.sql

		//when
		List<LocalDate> availableDates = bookingService.checkAvailabilities(
				LocalDate.of(2021, 1, 1),
				LocalDate.of(2021, 1, 10)
		);

		//expected
		List<LocalDate> expectedDates = List.of(
				LocalDate.of(2021, 1, 2),
				LocalDate.of(2021, 1, 3),
				LocalDate.of(2021, 1, 6),
				LocalDate.of(2021, 1, 7)
		);

		//then
		Assert.assertEquals(expectedDates, availableDates);

	}

}
