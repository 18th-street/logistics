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
@SQLDelete(sql = "UPDATE p_product SET product_is_deleted = true WHERE product_id = ?")
@SQLRestriction("product_is_deleted = false")
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

	@Builder.Default
	@Column(name = "product_is_deleted")
	private Boolean isDeleted = Boolean.FALSE;

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

	public void performSoftDelete() {
		this.isDeleted = Boolean.TRUE;
		this.softDelete();
	}
}
