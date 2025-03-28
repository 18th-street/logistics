package com.eighteenthstreet.deliveryservice.application.event;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.eighteenthstreet.deliveryservice.domain.event.DeliveryCreatedEvent;
import com.eighteenthstreet.deliveryservice.infrastructure.messaging.message.DeliveryCancelledErrMessage;
import com.eighteenthstreet.deliveryservice.infrastructure.messaging.message.DeliveryCancelledEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class DeliveryEventPublisher {
	private final RabbitTemplate rabbitTemplate;

	@Value("${message.queue.delivery-service}")
	private String queueDelivery;

	@Value("${message.complete.queue.delivery.cancelled}")
	private String queueCompleteCancelledDelivery;

	@Value("${message.err.queue.delivery.cancelled}")
	private String queueErrDeliveryCancelled;

	// 배달 생성 이벤트 발행
	public void publishDeliveryCreated(DeliveryCreatedEvent event) {
		log.info("######### Send Message[Delivery] : {}", event);
		rabbitTemplate.convertAndSend(queueDelivery, event);
	}

	// 배달 취소 성공 이벤트 발행
	public void publishDeliveryCancelled(DeliveryCancelledEvent message) {
		log.info("######### Send Message[Delivery Cancelled] : {}", message);
		rabbitTemplate.convertAndSend(queueCompleteCancelledDelivery, message);
	}

	// 배달 취소 실패 이벤트 발행
	public void publishDeliveryCancelledError(DeliveryCancelledErrMessage message) {
		log.info("######### Send Message[Delivery Cancelled Error] : {}", message);
		rabbitTemplate.convertAndSend(queueErrDeliveryCancelled, message);
	}

}
