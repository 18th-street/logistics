package com.eigtheenthstreet.order_service.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eigtheenthstreet.order_service.domain.model.Order;
import com.eigtheenthstreet.order_service.domain.repository.OrderRepository;

@Repository
public interface JpaOrderRepository extends JpaRepository<Order, Long>, OrderRepository {
}
