package com.eighteenthstreet.product_service.application.dto;

import java.util.UUID;

import com.eighteenthstreet.product_service.domain.model.Product;

public record SelectProductResponse(
	UUID productId,
	UUID companyId,
	String name,
	String description,
	Integer quantity
) {
	public static SelectProductResponse from(Product foundProduct) {
		return new SelectProductResponse(
			foundProduct.getId(),
			foundProduct.getCompanyId(),
			foundProduct.getName(),
			foundProduct.getDescription(),
			foundProduct.getQuantity()
		);
	}
}
