package com.eigtheenthstreet.order_service.infrastructure.client.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateProductResponse(
	UUID productId,
	@JsonProperty("name")
	String productName,
	@JsonProperty("price")
	Integer productPrice,
	Boolean isSold,
	@JsonProperty("quantity")
	Integer stockQuantity
) {
}
