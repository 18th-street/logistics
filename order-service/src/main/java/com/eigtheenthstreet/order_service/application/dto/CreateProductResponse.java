package com.eigtheenthstreet.order_service.application.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateProductResponse(
	UUID productId,
	@JsonProperty("price")
	Integer productPrice,
	Boolean isSold,
	@JsonProperty("quantity")
	Integer stockQuantity
) {
}
