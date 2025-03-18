package com.eighteenthstreet.deliveryservice;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.eighteenthstreet.deliveryservice.presentation.request.CreateDeliveryRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.ws.rs.core.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class DeliveryControllerApiTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("배달 생성 Api")
	public void testCreateDelivery() throws Exception {

		CreateDeliveryRequest request = CreateDeliveryRequest.builder()
			.orderId(UUID.randomUUID())
			.startHubId(UUID.randomUUID())
			.endHubId(UUID.randomUUID())
			.recipient("박종민")
			.destinationAddress("황새울로 234")
			.recipientSlackId("test.com")
			.build();

		String requestJson = objectMapper.writeValueAsString(request);

		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/vi/deliveries")
					.content(requestJson)
					.contentType(MediaType.APPLICATION_JSON)
			).andDo(MockMvcResultHandlers.print())
			.andExpectAll(
				MockMvcResultMatchers.status().isOk()
			);
	}
}
