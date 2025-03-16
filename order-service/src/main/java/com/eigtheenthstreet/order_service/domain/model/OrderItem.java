package com.eigtheenthstreet.order_service.domain.model;

import java.util.UUID;

import base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "p_oderItem")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class OrderItem extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "orderItem_id")
	private UUID id;

	@Column(name = "orderItem_quantity")
	private Integer quantity;

	@Column(name = "orderItem_totalPrcie")
	private Integer totalPrice;

	@Column(name = "product_id")
	private UUID productId;

	@Column(name = "order_id")
	private UUID orderId;

	public static OrderItem create(UUID orderId, UUID productId, Integer productQuantity, Integer productPrice) {
		return OrderItem.builder()
			.orderId(orderId)
			.productId(productId)
			.quantity(productQuantity)
			.totalPrice(calculateTotalPrice(productQuantity, productPrice))
			.build();
	}

	private static int calculateTotalPrice(Integer productQuantity, Integer productPrice) {
		return productQuantity * productPrice;
	}
}
