package com.eigtheenthstreet.order_service.infrastructure.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.eigtheenthstreet.order_service.application.dto.CreateProductResponse;

@FeignClient(name = "product-service", url = "http://localhost:19096")
public interface ProductServiceClient {
	@GetMapping("/api/v1/products/{productId}")
	CreateProductResponse getProduct(@PathVariable UUID productId);

	// todo
	//void decreaseStock(UUID productId, Integer productQuantity);
}
