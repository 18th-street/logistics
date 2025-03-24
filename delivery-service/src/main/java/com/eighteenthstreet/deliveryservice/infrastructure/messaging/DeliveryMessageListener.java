package com.eighteenthstreet.deliveryservice.infrastructure.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.eighteenthstreet.deliveryservice.application.DeliveryService;
import com.eighteenthstreet.deliveryservice.application.dto.CreateDeliveryResponse;
import com.eighteenthstreet.deliveryservice.infrastructure.messaging.message.DeliveryCancelledEvent;
import com.eighteenthstreet.deliveryservice.infrastructure.messaging.message.DeliveryMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryMessageListener {
	private final DeliveryService deliveryService;

	@RabbitListener(queues = "${message.queue.delivery.created}")
	public ResponseEntity<CreateDeliveryResponse> createDelivery(DeliveryMessage message) {
		log.info("#### Delivery Message 수신 {} ", message);
		CreateDeliveryResponse response = deliveryService.createMessageDelivery(message);

		return ResponseEntity.ok(response);
	}

	@RabbitListener(queues = "${message.queue.delivery.cancelled}")
	public void cancelDelivery(DeliveryCancelledEvent message) {
		log.info("#### Delivery Cancel Message 수신 {} ", message);
		deliveryService.cancelledDelivery(message);
	}
}
