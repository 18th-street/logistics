package com.eighteenthstreet.product_service.application.dto;

import java.util.UUID;

import com.eighteenthstreet.product_service.domain.model.Product;

public record BulkProductResponse(
	UUID productId,
	String productName,
	Integer productPrice,
	Integer productQuantity,
	Boolean isSold
) {
	public static BulkProductResponse from(Product product) {
		return new BulkProductResponse(
			product.getId(),
			product.getName(),
			product.getPrice(),
			product.getQuantity(),
			product.isSold()
		);
	}
}
