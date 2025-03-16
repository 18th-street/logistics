package com.eigtheenthstreet.order_service.domain.repository;

import com.eigtheenthstreet.order_service.domain.model.OrderItem;

public interface OrderItemRepository {
	OrderItem save(OrderItem orderItem);
}
