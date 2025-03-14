package com.eighteenthstreet.deliveryservice.applicatoin;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eighteenthstreet.deliveryservice.applicatoin.dto.CreateDeliveryResponse;
import com.eighteenthstreet.deliveryservice.domain.exception.DeliveryNotFoundException;
import com.eighteenthstreet.deliveryservice.domain.model.Delivery;
import com.eighteenthstreet.deliveryservice.domain.model.DeliveryStatus;
import com.eighteenthstreet.deliveryservice.domain.repository.DeliveryRepository;
import com.eighteenthstreet.deliveryservice.presentation.exception.ErrorCode;
import com.eighteenthstreet.deliveryservice.presentation.request.CreateDeliveryRequest;
import com.eighteenthstreet.deliveryservice.presentation.request.UpdateStatusDeliveryRequest;

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

	@Transactional
	public void updateDeliveryStatus(UpdateStatusDeliveryRequest request) {
		Delivery delivery = deliveryRepository.findById(request.getDeliveryId()).orElseThrow(
			() -> new DeliveryNotFoundException(ErrorCode.DELIVERY_NOT_FOUND)
		);

		delivery.updateStatus(request.getDeliveryStatus());
		deliveryRepository.save(delivery);
	}
}
