package com.eigtheenthstreet.order_service.infrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eigtheenthstreet.order_service.domain.model.Order;
import com.eigtheenthstreet.order_service.domain.repository.OrderRepository;

@Repository
public interface JpaOrderRepository extends JpaRepository<Order, Long>, OrderRepository {
	// todo. 키워드 설정해서 리팩토링 -> ex. 주문 상품
	@Query("SELECT o FROM Order o "
		+ "JOIN FETCH OrderItem oi ON o.id = oi.orderId")
	Page<Order> searchByOrders(Pageable pageable);
}
