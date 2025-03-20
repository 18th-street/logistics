package com.eigtheenthstreet.order_service.infrastructure.client.dto;

import java.util.List;
import java.util.UUID;

public record GetBulkProductRequest(List<UUID> productIds) {
	public static GetBulkProductRequest from(List<UUID> productIds) {
		return new GetBulkProductRequest(productIds);
	}
}
