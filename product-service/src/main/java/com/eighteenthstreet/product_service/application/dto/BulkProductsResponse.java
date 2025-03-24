package com.eighteenthstreet.product_service.application.dto;

import java.util.List;

public record BulkProductsResponse(List<BulkProductResponse> bulkProductsResponse) {
}
