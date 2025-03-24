package com.eigtheenthstreet.order_service.infrastructure.client.dto;

import java.util.List;

public record GetBulkProductsResponse(List<GetBulkProductResponse> bulkProductsResponse) {
}
