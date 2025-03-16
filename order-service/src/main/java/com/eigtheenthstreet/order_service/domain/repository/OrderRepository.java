package com.eigtheenthstreet.order_service.domain.repository;

import java.util.Optional;
import java.util.UUID;

import com.eigtheenthstreet.order_service.domain.model.Order;

public interface OrderRepository {
	Order save(Order order);

	Optional<Order> findById(UUID orderId);
}
