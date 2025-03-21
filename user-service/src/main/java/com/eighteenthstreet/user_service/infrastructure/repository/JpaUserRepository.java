package com.eighteenthstreet.user_service.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eighteenthstreet.user_service.domain.model.User;

public interface JpaUserRepository extends JpaRepository<User, UUID> {
	User findByUsername(String username);

	@Query(value = "SELECT u FROM User u "
		+ "WHERE (COALESCE(:name, '') = '' OR LOWER(u.username) LIKE LOWER(CONCAT('%', :name, '%')))")
	Page<User> findActiveUsers(@Param("name") String name, Pageable pageable);

	User findByUserId(UUID userId);
}
