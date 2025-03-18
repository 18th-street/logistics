package com.eigtheenthstreet.order_service.domain.model;

import java.util.UUID;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.eigtheenthstreet.order_service.presentation.request.UpdateOrderRequest;

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
@SQLDelete(sql = "UPDATE p_oder_item SET is_deleted = true, deleted_at = now() WHERE order_item_id = ?")
@SQLRestriction("is_deleted = false")
public class OrderItem extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "orderItem_id")
	private UUID id;

	@Column(name = "orderItem_quantity")
	private Integer quantity;

	@Column(name = "orderItem_totalPrice")
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

	public void update(UpdateOrderRequest.UpdateOrderItemRequest orderItemRequest, Integer productPrice) {
		this.quantity += orderItemRequest.productQuantity();
		this.totalPrice = this.quantity * productPrice;
	}

	public void performSoftDelete() {
		this.softDelete();
	}
}
