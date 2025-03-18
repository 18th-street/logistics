package com.eighteenthstreet.deliveryservice.presentation.request;

import java.util.UUID;

import com.eighteenthstreet.deliveryservice.domain.model.DeliveryStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UpdateStatusDeliveryRequest {
	private UUID deliveryId;
	private DeliveryStatus deliveryStatus;
}
