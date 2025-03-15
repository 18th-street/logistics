package com.eighteenthstreet.product_service.application.dto;

import java.util.UUID;

import com.eighteenthstreet.product_service.domain.model.Product;

public record CreateProductResponse(
	UUID productId
) {
	public static CreateProductResponse from(Product product) {
		return new CreateProductResponse(product.getId());
	}
}
