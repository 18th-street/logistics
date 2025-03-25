package com.eigtheenthstreet.order_service.infrastructure.messaging;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.eigtheenthstreet.order_service.domain.model.Order;
import com.eigtheenthstreet.order_service.infrastructure.messaging.message.DeliveryCancelledEvent;
import com.eigtheenthstreet.order_service.infrastructure.messaging.message.DeliveryCreatedEvent;
import com.eigtheenthstreet.order_service.infrastructure.messaging.message.NotificationEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderMessagePublisher {
	private final RabbitTemplate rabbitTemplate;

	@Value("${message.queue.delivery.created}")
	private String deliveryCreatedQueue;

	@Value("${message.queue.delivery.cancelled}")
	private String deliveryCancelledQueue;

	@Value("${message.queue.slack}")
	private String slackQueue;

	public void publishDeliveryCreated(
		Order order,
		UUID startHubId,
		UUID endHubId
	) {
		DeliveryCreatedEvent deliveryCreatedEvent = DeliveryCreatedEvent.of(order, startHubId, endHubId);
		rabbitTemplate.convertAndSend(deliveryCreatedQueue, deliveryCreatedEvent);
	}

	public void publishDeliveryCancellation(
		UUID orderId,
		UUID deliveryId
	) {
		DeliveryCancelledEvent deliveryCancelledEvent = DeliveryCancelledEvent.from(orderId, deliveryId);
		rabbitTemplate.convertAndSend(deliveryCancelledQueue, deliveryCancelledEvent);
	}

	public void publishNotification(UUID orderId) {
		NotificationEvent notificationEvent = NotificationEvent.from(orderId);
		rabbitTemplate.convertAndSend(slackQueue, notificationEvent);
	}
}
