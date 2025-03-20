package com.eigtheenthstreet.order_service.domain.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eigtheenthstreet.order_service.domain.model.Order;
import com.eigtheenthstreet.order_service.domain.model.OrderItem;
import com.eigtheenthstreet.order_service.domain.model.OrderStatus;
import com.eigtheenthstreet.order_service.domain.repository.OrderItemRepository;
import com.eigtheenthstreet.order_service.domain.repository.OrderRepository;
import com.eigtheenthstreet.order_service.exception.order.CustomOrderItemNotFoundException;
import com.eigtheenthstreet.order_service.exception.order.CustomOrderNotFoundException;
import com.eigtheenthstreet.order_service.presentation.request.CreateOrderRequest;
import com.eigtheenthstreet.order_service.util.DateTimeUtil;

import exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderDomainService {
	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;

	@Transactional
	public Order createOrder(CreateOrderRequest request, UUID userId) {
		Order order = Order.builder()
			.supplierCompanyId(request.supplierCompanyId())
			.consumerCompanyId(request.consumerCompanyId())
			.orderStatus(OrderStatus.CREATED)
			.ordererId(userId)
			.requestDetails(request.requestDetails())
			.destinationAddress(request.deliveryAddress())
			.deliveryLimitedAt(DateTimeUtil.parseDateTime(request.deliveryLimitedAt()))
			.build();

		return orderRepository.save(order);
	}

	@Transactional(readOnly = true)
	public Order getOrderById(UUID orderId) {
		return getOrder(orderId);
	}

	@Transactional
	public OrderItem createOrderItem(
		UUID orderId,
		UUID productId,
		int quantity,
		int totalPrice
	) {
		OrderItem orderItem = OrderItem.create(
			orderId,
			productId,
			quantity,
			totalPrice
		);

		return orderItemRepository.save(orderItem);
	}

	@Transactional
	public int updateOrderItem(
		UUID orderId,
		UUID productId,
		int newQuantity,
		int productPrice
	) {
		Order order = findOrderById(orderId);
		List<OrderItem> orderItems = order.getOrderItems();

		if (orderItems == null) {
			throw new CustomOrderItemNotFoundException(ErrorCode.ORDER_ITEM_NOT_FOUND);
		}

		Optional<OrderItem> existingOrderItem = orderItems.stream()
			.filter(item -> item.getProductId().equals(productId))
			.findFirst();

		int quantityDifference = 0;

		if (existingOrderItem.isPresent()) {
			// 기존 상품 업데이트
			OrderItem orderItem = existingOrderItem.get();
			int oldQuantity = orderItem.getQuantity();
			quantityDifference = oldQuantity - newQuantity; // +: 감소, -: 증가
			orderItem.updateQuantityAndTotalPrice(newQuantity, productPrice);
		} else {
			// 새로운 상품 추가
			OrderItem newOrderItem = OrderItem.create(orderId, productId, newQuantity, productPrice);
			orderItemRepository.save(newOrderItem);
			orderItems.add(newOrderItem);
			quantityDifference = -newQuantity; // 새 상품은 항상 수량 증가
		}

		order.updateOrderTotal(orderItems);
		return quantityDifference;
	}

	@Transactional(readOnly = true)
	public List<OrderItem> getOrderItems(UUID orderId) {
		Order order = getOrder(orderId);

		List<OrderItem> orderItems = order.getOrderItems();

		if (orderItems == null) {
			throw new CustomOrderItemNotFoundException(ErrorCode.ORDER_ITEM_NOT_FOUND);
		}

		return orderItems;
	}

	public void updateOrderQuantityAndTotalAmount(UUID orderId, List<OrderItem> orderItems) {
		int totalQuantity = orderItems.stream().mapToInt(OrderItem::getQuantity).sum();
		int totalPrice = orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();

		Order order = findOrderById(orderId);
		order.saveOrderTotalQuantityAndTotalAmount(totalQuantity, totalPrice);
	}

	private Order getOrder(UUID orderId) {
		return orderRepository.findById(orderId)
			.orElseThrow(() -> new CustomOrderNotFoundException(ErrorCode.ORDER_NOT_FOUND));
	}

	private Order findOrderById(UUID orderId) {
		return orderRepository.findById(orderId)
			.orElseThrow(() -> new CustomOrderNotFoundException(ErrorCode.ORDER_NOT_FOUND));
	}
}
