package com.eigtheenthstreet.order_service.infrastructure.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.eigtheenthstreet.order_service.application.OrderService;
import com.eigtheenthstreet.order_service.infrastructure.messaging.message.DeliveryCancelledCompleteMessage;
import com.eigtheenthstreet.order_service.infrastructure.messaging.message.DeliveryCancelledErrMessage;
import com.eigtheenthstreet.order_service.infrastructure.messaging.message.DeliveryCreatedCompleteMessage;
import com.eigtheenthstreet.order_service.infrastructure.messaging.message.DeliveryCreatedErrMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderMessageListener {
	private final OrderService orderService;

	// 배송 생성 이벤트를 수신한 경우 (성공)
	@RabbitListener(queues = "${message.complete.queue.delivery.created}")
	public void completeDeliveryCreated(DeliveryCreatedCompleteMessage message) {
		log.info("[OrderMessageListener] complete.queue.delivery.created 큐에 orderId={}, deliveryId={}를 수신 받았습니다.",
			message.orderId(), message.deliveryId());

		orderService.handleDeliveryCompleteCreated(message.orderId(), message.deliveryId());
	}

	// 배송 생성 이벤트를 수신한 경우 (실패)
	@RabbitListener(queues = "${message.err.queue.delivery.created}")
	public void errDeliveryCreated(DeliveryCreatedErrMessage message) {
		log.info("[OrderMessageListener] err.queue.delivery.created 큐에 orderId={}를 수신 받았습니다.", message.orderId());

		orderService.handleDeliveryErrCreated(message.orderId());
	}

	// 배송 취소 이벤트를 수신한 경우 (성공)
	@RabbitListener(queues = "${message.complete.queue.delivery.cancelled}")
	public void completeDeliveryCancel(DeliveryCancelledCompleteMessage message) {
		log.info("[OrderMessageListener] complete.queue.delivery.cancelled 큐에 orderId={}, deliveryId={}를 수신 받았습니다.",
			message.orderId(), message.deliveryId());

		orderService.handleDeliveryCompleteCancelled(message.orderId(), message.deliveryId());
	}

	// 배송 취소 이벤트를 수신한 경우 (실패)
	@RabbitListener(queues = "${message.err.queue.delivery.cancelled}")
	public void errDeliveryCancel(DeliveryCancelledErrMessage message) {
		log.info("[OrderMessageListener] err.queue.delivery.cancelled 큐에 orderId={}, failureReason={}를 수신 받았습니다.",
			message.orderId(), message.failureReason());
		
		orderService.handleDeliveryErrCancelled(message.orderId());
	}
}
