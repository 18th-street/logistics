package com.eigtheenthstreet.order_service.domain.model;

import java.util.UUID;

import com.eigtheenthstreet.order_service.presentation.request.CreateOrderRequest;

import base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_orders")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Order extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "order_id")
	private UUID id;

	@Column(name = "order_quantity")
	private Integer quantity;

	@Column(name = "order_totalPrice")
	private Integer totalAmount;

	@Column(name = "order_request_details")
	private String requestDetails;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	@Column(name = "delivery_id")
	private UUID deliveryId;

	@Column(name = "orderer_id")
	private UUID ordererId;

	@Column(name = "supplier_company_id")
	private UUID supplierCompanyId;

	@Column(name = "consumer_company_id")
	private UUID consumerCompanyId;

	public static Order create(CreateOrderRequest request, Long userId) {
		return Order.builder()
			.supplierCompanyId(request.supplierCompanyId())
			.consumerCompanyId(request.consumerCompanyId())
			.requestDetails(request.requestDetails())
			.orderStatus(OrderStatus.CREATED)
			//.ordererId(userId)
			.build();
	}

	// public void addDelivery(UUID deliveryId) {
	// 	this.deliveryId = deliveryId;
	// }

	public void addOrderTotalQuantityAndTotalAmount(int totalQuantity, Integer totalAmount) {
		this.quantity = totalQuantity;
		this.totalAmount = totalAmount;
	}

	// public void changeOrderStatusFailed() {
	// 	this.orderStatus = OrderStatus.DELIVERY_FAILED;
	// }
}
