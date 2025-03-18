package com.eighteenthstreet.user_service.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eighteenthstreet.user_service.domain.model.User;

public interface UserRepository {
	void save(User user);

	boolean isExistUsername(String username);

	Page<User> findActiveUsers(String name, Pageable pageable);

	User findByUsername(String username);

	User findById(Long userId);
}
