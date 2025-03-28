package com.eighteenthstreet.deliveryservice.application.event;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eighteenthstreet.deliveryservice.domain.event.DeliveryAgentAssignedEvent;
import com.eighteenthstreet.deliveryservice.domain.event.DeliveryFailedEvent;
import com.eighteenthstreet.deliveryservice.domain.event.DeliveryRouteCreationFailedEvent;
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
public class DeliveryEventHandler {
	private final DeliveryRepository deliveryRepository;

	@RabbitListener(queues = "${message.queue.delivery-assigned}")
	public void handleDeliveryAgentAssignedEvent(DeliveryAgentAssignedEvent event) {
		log.info("DeliveryAgentEvent 수신 {}", event);

		try {
			Delivery delivery = deliveryRepository.findById(event.deliveryId())
				.orElseThrow(() -> new DeliveryNotFoundException(ErrorCode.DELIVERY_NOT_FOUND));

			delivery.setStatus(DeliveryStatus.OUT_FOR_DELIVERY);  // 상태 변경

			deliveryRepository.save(delivery);
			log.info("Delivery 상태 업데이트 완료");
		} catch (Exception e) {
			log.error("Delivery 상태 업데이트 중 오류: event={}, 오류: {}", event, e.getMessage(), e);
			throw e;
		}

	}

	@RabbitListener(queues = "${message.queue.delivery-route-failed}")
	@Transactional
	public void handleDeliveryRouteCreationFailed(DeliveryRouteCreationFailedEvent event) {
		log.info("배송 경로 생성 실패 이벤트 수신: {}", event);
		Delivery delivery = deliveryRepository.findById(event.deliveryId()).orElseThrow(
			() -> new DeliveryNotFoundException(ErrorCode.DELIVERY_NOT_FOUND)
		);

		delivery.failed();
		deliveryRepository.save(delivery);

	}

	@RabbitListener(queues = "${message.queue.delivery-agent-failed}")
	@Transactional
	public void handleDeliveryAgentCreationFailed(DeliveryFailedEvent event) {
		Delivery delivery = deliveryRepository.findById(event.deliveryId()).orElseThrow(
			() -> new DeliveryNotFoundException(ErrorCode.DELIVERY_NOT_FOUND)
		);

		delivery.failed();
		deliveryRepository.save(delivery);

		log.info("배송 담당자 실패 이벤트 수신: {}", event);
	}
}
