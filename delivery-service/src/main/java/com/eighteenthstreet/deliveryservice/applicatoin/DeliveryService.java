package com.eighteenthstreet.deliveryservice.applicatoin;

import org.springframework.stereotype.Service;

import com.eighteenthstreet.deliveryservice.applicatoin.dto.CreateDeliveryResponse;
import com.eighteenthstreet.deliveryservice.domain.model.Delivery;
import com.eighteenthstreet.deliveryservice.domain.model.DeliveryStatus;
import com.eighteenthstreet.deliveryservice.domain.repository.DeliveryRepository;
import com.eighteenthstreet.deliveryservice.presentation.request.CreateDeliveryRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryService {
	private final DeliveryRepository deliveryRepository;

	public CreateDeliveryResponse createDelivery(CreateDeliveryRequest createDeliveryRequest) {
		Delivery delivery = Delivery.builder()
			.startHubId(createDeliveryRequest.getStartHubId())
			.endHubId(createDeliveryRequest.getEndHubId())
			.orderId(createDeliveryRequest.getOrderId())
			.recipient(createDeliveryRequest.getRecipient())
			.destinationAddress(createDeliveryRequest.getDestinationAddress())
			.slackId(createDeliveryRequest.getRecipientSlackId())
			.status(DeliveryStatus.PENDING_AT_HUB)
			.build();

		return CreateDeliveryResponse.fromEntity(deliveryRepository.save(delivery));
	}
}
