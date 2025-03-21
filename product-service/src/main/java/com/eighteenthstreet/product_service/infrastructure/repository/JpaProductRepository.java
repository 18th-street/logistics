package com.eighteenthstreet.product_service.infrastructure.repository;

import java.util.List;
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

	@Query("SELECT p FROM Product p "
		+ "WHERE (:query IS NULL or p.name LIKE %:query%)")
	Page<Product> searchByProducts(String query, Pageable pageable);

	@Query("SELECT p FROM Product p "
		+ "WHERE p.id IN :productIds")
	List<Product> findByIds(List<UUID> productIds);
}
