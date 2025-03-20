package com.eigtheenthstreet.order_service.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.eigtheenthstreet.order_service.presentation.request.CreateOrderRequest;
import com.eigtheenthstreet.order_service.util.DateTimeUtil;

import base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@SQLDelete(sql = "UPDATE p_orders SET is_deleted = true, deleted_at = now() WHERE order_id = ?")
@SQLRestriction("is_deleted = false")
public class Order extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "order_id")
	private UUID id;

	@Column(name = "order_quantity")
	private Integer quantity;

	@Column(name = "order_totalPrice")
	private Integer totalAmount;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	@Column(name = "delivery_id")
	private UUID deliveryId;

	@Column(name = "orderer_id")
	private UUID ordererId;

	@Column(name = "supplier_company_id")
	private UUID supplierCompanyId;

	@Column(name = "receiver_company_id")
	private UUID consumerCompanyId;

	@Column(name = "order_destination_address")
	private String destinationAddress;

	@Column(name = "order_request_details")
	private String requestDetails;

	@Column(name = "order_delivery_limited_at")
	private LocalDateTime deliveryLimitedAt;

	@Builder.Default
	@OneToMany(mappedBy = "orderId")
	private List<OrderItem> orderItems = new ArrayList<>();

	public static Order create(CreateOrderRequest request, UUID userId) {
		return Order.builder()
			.supplierCompanyId(request.supplierCompanyId())
			.consumerCompanyId(request.consumerCompanyId())
			.orderStatus(OrderStatus.CREATED)
			.ordererId(userId)
			.requestDetails(request.requestDetails())
			.destinationAddress(request.deliveryAddress())
			.deliveryLimitedAt(DateTimeUtil.parseDateTime(request.deliveryLimitedAt()))
			.build();
	}

	public void updateDeliveryId(UUID deliveryId) {
		this.deliveryId = deliveryId;
	}

	public void updateOrderTotal(List<OrderItem> orderItems) {
		this.quantity = orderItems.stream()
			.mapToInt(OrderItem::getQuantity)
			.sum();

		this.totalAmount = orderItems.stream()
			.mapToInt(OrderItem::getTotalPrice)
			.sum();
	}

	// public void addDelivery(UUID deliveryId) {
	// 	this.deliveryId = deliveryId;
	// }

	// public void changeOrderStatusFailed() {
	// 	this.orderStatus = OrderStatus.DELIVERY_FAILED;
	// }

	public void saveOrderTotalQuantityAndTotalAmount(int totalQuantity, int totalAmount) {
		this.quantity = totalQuantity;
		this.totalAmount = totalAmount;
	}

	public void performSoftDelete() {
		this.orderStatus = OrderStatus.CANCELLED;
		this.softDelete();
	}

	public void cancel() {
		performSoftDelete();
	}
}
