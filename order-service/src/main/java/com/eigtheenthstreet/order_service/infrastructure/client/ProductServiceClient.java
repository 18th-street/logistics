package com.eigtheenthstreet.order_service.infrastructure.client;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.eigtheenthstreet.order_service.infrastructure.client.dto.CreateProductResponse;
import com.eigtheenthstreet.order_service.infrastructure.client.dto.GetBulkProductRequest;
import com.eigtheenthstreet.order_service.infrastructure.client.dto.GetBulkProductResponse;

@FeignClient(name = "product-service", url = "http://localhost:19096")
public interface ProductServiceClient {
	@GetMapping("/api/v1/products/{productId}")
	CreateProductResponse getProduct(@PathVariable UUID productId);

	@PutMapping("/api/v1/products/{productId}/stock/decrease")
	void decreaseStock(@PathVariable UUID productId, @RequestParam int quantity);

	@PutMapping("/api/v1/products/{productId}/stock/restore")
	void restoreStock(@PathVariable UUID productId, @RequestParam int quantity);

	@PostMapping("/api/v1/products/bulk")
	List<GetBulkProductResponse> getBulkProducts(@RequestBody GetBulkProductRequest request);
}
