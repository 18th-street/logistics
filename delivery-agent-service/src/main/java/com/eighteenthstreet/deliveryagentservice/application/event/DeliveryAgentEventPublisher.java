package com.eighteenthstreet.deliveryagentservice.application.event;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.eighteenthstreet.deliveryagentservice.domain.event.DeliveryAgentAssignedEvent;
import com.eighteenthstreet.deliveryagentservice.domain.event.DeliveryFailedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeliveryAgentEventPublisher {
	private final RabbitTemplate rabbitTemplate;

	@Value("${message.queue.delivery-assigned}")
	private String deliveryAssignedQueue;

	@Value("${message.queue.delivery-agent-failed}")
	private String deliveryAssignedFailQueue;

	public void publishDeliveryAgentAssignedEvent(DeliveryAgentAssignedEvent event) {
		log.info("배달 서비스에 이벤트 발송: {}", event);
		rabbitTemplate.convertAndSend(deliveryAssignedQueue, event);
	}

	public void publishDeliveryFailedEvent(DeliveryFailedEvent event, Exception e) {
		rabbitTemplate.convertAndSend(deliveryAssignedFailQueue, event);
		log.error("배차 처리 중 오류 발생: event={}, 오류: {}", event, e.getMessage(), e);
	}

}
