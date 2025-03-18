package com.eighteenthstreet.product_service.presentation.request;

public record UpdateProductRequest(
	String name,
	String description,
	Integer quantity
) {
}
