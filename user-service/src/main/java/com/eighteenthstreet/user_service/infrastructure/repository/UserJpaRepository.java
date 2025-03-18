package com.eighteenthstreet.user_service.infrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eighteenthstreet.user_service.domain.model.User;

public interface UserJpaRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);

	@Query(value = "SELECT u FROM User u "
		+ "WHERE (COALESCE(:name, '') = '' OR LOWER(u.username) LIKE LOWER(CONCAT('%', :name, '%')))")
	Page<User> findActiveUsers(@Param("name") String name, Pageable pageable);
}