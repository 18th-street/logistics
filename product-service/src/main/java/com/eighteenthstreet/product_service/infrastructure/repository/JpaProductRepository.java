package com.eighteenthstreet.product_service.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eighteenthstreet.product_service.domain.model.Product;
import com.eighteenthstreet.product_service.domain.repository.ProductRepository;

@Repository
public interface JpaProductRepository extends JpaRepository<Product, UUID>, ProductRepository {
	boolean existsByName(String name);

	Optional<Product> findById(UUID productId);

	@Query("select p from Product p "
		+ "where (:query is null or p.name like %:query%)")
	Page<Product> searchByProducts(String query, Pageable pageable);
}
