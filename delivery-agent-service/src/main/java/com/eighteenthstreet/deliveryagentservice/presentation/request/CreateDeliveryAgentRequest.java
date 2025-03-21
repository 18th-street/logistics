package com.eighteenthstreet.deliveryagentservice.presentation.request;

import java.util.UUID;

import com.eighteenthstreet.deliveryagentservice.domain.model.DeliveryAgentType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateDeliveryAgentRequest {

	private UUID hubId;
	private DeliveryAgentType deliveryAgentType;
	private String slackId;
}
