package com.eigtheenthstreet.order_service.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eigtheenthstreet.order_service.domain.model.Order;

public interface OrderRepository {
	Order save(Order order);

	Optional<Order> findById(UUID orderId);

	Page<Order> searchByOrders(Pageable pageable);
}
