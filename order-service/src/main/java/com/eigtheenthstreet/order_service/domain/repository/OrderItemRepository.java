package com.eigtheenthstreet.order_service.domain.repository;

import java.util.List;
import java.util.UUID;

import com.eigtheenthstreet.order_service.domain.model.OrderItem;

public interface OrderItemRepository {
	OrderItem save(OrderItem orderItem);

	List<OrderItem> findByOrderId(UUID orderId);
}
