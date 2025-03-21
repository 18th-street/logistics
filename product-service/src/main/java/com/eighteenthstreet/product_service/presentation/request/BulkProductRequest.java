package com.eighteenthstreet.product_service.presentation.request;

import java.util.List;
import java.util.UUID;

public record BulkProductRequest(List<UUID> productIds) {
}
