package com.eighteenthstreet.deliveryrouteservice.application.event;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.eighteenthstreet.deliveryrouteservice.domain.event.DeliveryRouteCreationFailedEvent;
import com.eighteenthstreet.deliveryrouteservice.domain.event.OrderDeliveryCompleteMessage;
import com.eighteenthstreet.deliveryrouteservice.domain.event.RouteCreatedEvent;
import com.eighteenthstreet.deliveryrouteservice.infrastructure.messaging.DeliveryCreatedErrMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class DeliveryRouteEventPublisher {

	private final RabbitTemplate rabbitTemplate;

	@Value("${message.queue.delivery-route}")
	private String queue;

	@Value("${message.queue.delivery-route-failed}")
	private String failedQueue;

	@Value("${message.complete.queue.delivery.created}")
	private String completeOrderQueue;

	@Value("${message.err.queue.delivery.created}")
	private String errOrderCreatedQueue;

	public void publishRouteCreated(RouteCreatedEvent event) {
		rabbitTemplate.convertAndSend(queue, event);
		log.info("##### 배차 요청 보냄: {}", event);
	}

	public void publishOrderDeliveryComplete(OrderDeliveryCompleteMessage message) {
		rabbitTemplate.convertAndSend(completeOrderQueue, message);
		log.info("###### 주문 측에 성공 메세지 보냄: {}", message);
	}

	public void publishDeliveryRouteCreationFailed(DeliveryRouteCreationFailedEvent event) {
		rabbitTemplate.convertAndSend(failedQueue, event);
		log.info("배송 경로 생성 실패 이벤트 발송: {}", event);
	}

	public void publishDeliveryCreatedError(DeliveryCreatedErrMessage message) {
		rabbitTemplate.convertAndSend(errOrderCreatedQueue, message);
		log.info("배달 생성 실패 이벤트 발송: {}", message);
	}
}