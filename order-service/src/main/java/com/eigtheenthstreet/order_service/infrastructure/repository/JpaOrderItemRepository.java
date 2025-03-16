package com.eigtheenthstreet.order_service.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eigtheenthstreet.order_service.domain.model.OrderItem;
import com.eigtheenthstreet.order_service.domain.repository.OrderItemRepository;

@Repository
public interface JpaOrderItemRepository extends JpaRepository<OrderItem, UUID>, OrderItemRepository {
}
