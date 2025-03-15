package com.eighteenthstreet.product_service.presentation.request;

import java.util.UUID;

public record CreateProductRequest(
	UUID companyId,
	UUID hubId,
	String name,
	String description,
	Integer quantity
) {
}
