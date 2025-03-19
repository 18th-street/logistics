package com.eighteenthstreet.deliveryservice.application;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.eighteenthstreet.deliveryservice.domain.event.DeliveryAgentAssignedEvent;
import com.eighteenthstreet.deliveryservice.domain.exception.DeliveryNotFoundException;
import com.eighteenthstreet.deliveryservice.domain.model.Delivery;
import com.eighteenthstreet.deliveryservice.domain.model.DeliveryStatus;
import com.eighteenthstreet.deliveryservice.domain.repository.DeliveryRepository;

import exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliveryEventService {
	private final DeliveryRepository deliveryRepository;

	@RabbitListener(queues = "${message.queue.delivery-assigned}")
	public void handleDeliveryAgentAssignedEvent(DeliveryAgentAssignedEvent event) {
		log.info("DeliveryAgentEvent 수신 {}", event);

		try {
			Delivery delivery = deliveryRepository.findById(event.deliveryId()).orElseThrow(
				() -> new DeliveryNotFoundException(ErrorCode.DELIVERY_NOT_FOUND)
			);

			delivery.setStatus(DeliveryStatus.OUT_FOR_DELIVERY);  // 상태 변경

			deliveryRepository.save(delivery);
			log.info("Delivery 상태 업데이트 완료");
		} catch (Exception e) {
			log.error("Delivery 상태 업데이트 중 오류: event={}, 오류: {}", event, e.getMessage(), e);
			throw e;
		}

	}
}
