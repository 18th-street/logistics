package com.eigtheenthstreet.order_service.infrastructure.client.dto;

import java.util.UUID;

public record GetBulkProductResponse(
	UUID productId,
	String productName,
	Integer productPrice,
	Integer productQuantity,
	Boolean isSold
) {
}
