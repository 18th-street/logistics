package com.eighteenthstreet.product_service.application.dto;

import java.util.UUID;

import com.eighteenthstreet.product_service.domain.model.Product;

public record UpdateProductResponse(
	UUID productId,
	UUID companyId,
	String name,
	String description,
	Integer quantity
) {
	public static UpdateProductResponse from(Product foundProduct) {
		return new UpdateProductResponse(
			foundProduct.getId(),
			foundProduct.getCompanyId(),
			foundProduct.getName(),
			foundProduct.getDescription(),
			foundProduct.getQuantity()
		);
	}
}
