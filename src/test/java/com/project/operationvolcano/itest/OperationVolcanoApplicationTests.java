package com.project.operationvolcano.itest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.operationvolcano.booking.api.model.DateRangeDto;
import com.project.operationvolcano.booking.api.model.ReservationConfirmationDto;
import com.project.operationvolcano.booking.api.model.ReservationDateRangeDto;
import com.project.operationvolcano.booking.api.model.ReservationDto;
import org.junit.ClassRule;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class OperationVolcanoApplicationTests {

	private static final String APPLICATION_JSON = "application/json";
	private static final String BOOKING_API = "/booking/v1";

	@ClassRule
	public static PostgreSQLContainer postgreSQLContainer = PostgresTestContainer.getInstance();

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void contextLoads() {
	}

	@Test
	void givenStartDateBeforeEndDate_whengettingAvailabilities_return400() throws Exception {

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

}
