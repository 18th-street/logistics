package com.eigtheenthstreet.order_service.infrastructure.messaging;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.eigtheenthstreet.order_service.domain.model.Order;
import com.eigtheenthstreet.order_service.infrastructure.messaging.message.DeliveryMessage;
import com.eigtheenthstreet.order_service.infrastructure.messaging.message.SlackMessage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderMessageProducer {
	private final RabbitTemplate rabbitTemplate;

	@Value("${message.queue.delivery}")
	private String deliveryQueue;

	@Value("${message.queue.slack}")
	private String slackQueue;

	public void sendToDelivery(
		Order order,
		UUID startHubId,
		UUID endHubId
	) {
		DeliveryMessage deliveryMessage = DeliveryMessage.of(order, startHubId, endHubId);
		rabbitTemplate.convertAndSend(deliveryQueue, deliveryMessage);
	}

	public void sendToSlack(Order order) {
		SlackMessage slackMessage = SlackMessage.from(order);
		rabbitTemplate.convertAndSend(slackQueue, slackMessage);
	}
}
