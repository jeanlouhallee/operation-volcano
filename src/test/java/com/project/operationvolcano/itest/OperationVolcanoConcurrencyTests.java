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
@DisplayName("Operation volcano integration tests")
class OperationVolcanoConcurrencyTests {

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
	void test1() throws Exception {

		//given
		ReservationDto requestBody = ReservationDto.builder()
				.firstName("Peter")
				.lastName("Parker")
				.email("spider@man.com")
				.stayDates(new ReservationDateRangeDto(LocalDate.now().plusDays(1), LocalDate.now().plusDays(4)))
				.build();

		MvcResult result = mockMvc.perform(post(BOOKING_API + "/reservation")
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestBody)))
				.andExpect(status().isCreated())
				.andReturn();

	}

	@Test
	void test2() throws Exception {

		//given
		ReservationDto requestBody = ReservationDto.builder()
										.firstName("Tony")
										.lastName("Stark")
										.email("iron@man.com")
										.stayDates(new ReservationDateRangeDto(LocalDate.now().plusDays(2), LocalDate.now().plusDays(3)))
										.build();

		MvcResult result = mockMvc.perform(post(BOOKING_API + "/reservation")
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestBody)))
				.andExpect(status().isCreated())
				.andReturn();

	}
}
