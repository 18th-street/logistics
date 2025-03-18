package com.eighteenthstreet.product_service.domain.model;

import java.util.UUID;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.eighteenthstreet.product_service.presentation.request.CreateProductRequest;
import com.eighteenthstreet.product_service.presentation.request.UpdateProductRequest;

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
@Table(name = "p_product")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE p_product SET is_deleted = true, deleted_at = now() WHERE product_id = ?")
@SQLRestriction("is_deleted = false")
public class Product extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "product_id")
	private UUID id;

	@Column(name = "product_name")
	private String name;

	@Column(name = "product_description")
	private String description;

	@Column(name = "product_quantity")
	private Integer quantity;

	@Column(name = "product_price")
	private Integer price;

	@Builder.Default
	@Column(name = "product_isSold")
	private boolean isSold = Boolean.TRUE;

	@Column(name = "company_id")
	private UUID companyId;

	public static Product create(CreateProductRequest request) {
		return Product.builder()
			.name(request.name())
			.description(request.description())
			.quantity(request.quantity())
			.price(request.price())
			.companyId(request.companyId())
			.build();
	}

	public void update(UpdateProductRequest request) {
		this.name = request.name();
		this.description = request.description();
		this.quantity = request.quantity();
	}

	public void decreaseStock(int quantity) {
		if (quantity <= 0) {
			throw new IllegalArgumentException("주문 수량이 0 이하일 수 없습니다.");
		}

		if (this.quantity < quantity) {
			throw new IllegalArgumentException("상품 재고가 부족합니다.");
		}
		this.quantity -= quantity;
	}

	public void restoreStock(int quantity) {
		if (quantity <= 0) {
			throw new IllegalArgumentException("복원 수량이 0 이하일 수 없습니다.");
		}

		this.quantity += quantity;
	}

	public void performSoftDelete() {
		this.isSold = Boolean.FALSE;
		this.softDelete();
	}
}
