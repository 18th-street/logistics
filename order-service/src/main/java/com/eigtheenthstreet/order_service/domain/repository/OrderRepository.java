package com.eigtheenthstreet.order_service.domain.repository;

import com.eigtheenthstreet.order_service.domain.model.Order;

public interface OrderRepository {
	Order save(Order order);
}
