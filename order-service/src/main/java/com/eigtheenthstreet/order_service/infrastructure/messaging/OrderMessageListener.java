package com.eigtheenthstreet.order_service.infrastructure.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.eigtheenthstreet.order_service.domain.model.Order;
import com.eigtheenthstreet.order_service.domain.repository.OrderRepository;
import com.eigtheenthstreet.order_service.exception.order.CustomOrderNotFoundException;
import com.eigtheenthstreet.order_service.infrastructure.messaging.message.OrderDeliveryCompleteMessage;

import exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderMessageListener {
	private final OrderRepository orderRepository;

	// 배송 생성 이벤트를 수신한 경우 (성공)
	@RabbitListener(queues = "${message.complete.queue.order}")
	public void completeOrder(OrderDeliveryCompleteMessage message) {
		log.info("[OrderMessageListener] complete.order 큐에 {}를 수신 받았습니다.", message);

		Order order = orderRepository.findById(message.orderId())
			.orElseThrow(() -> new CustomOrderNotFoundException(ErrorCode.ORDER_NOT_FOUND));

		order.updateDeliveryId(message.deliveryId());
	}

}
