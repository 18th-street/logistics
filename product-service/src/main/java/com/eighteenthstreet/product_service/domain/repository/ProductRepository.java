package com.eighteenthstreet.product_service.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eighteenthstreet.product_service.domain.model.Product;

public interface ProductRepository {
	boolean existsByName(String name);

	Product save(Product product);

	Optional<Product> findById(UUID productId);

	Page<Product> searchByProducts(String query, Pageable pageable);

	List<Product> findByIds(List<UUID> productIds);
}
