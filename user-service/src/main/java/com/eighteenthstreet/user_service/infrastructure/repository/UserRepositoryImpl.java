package com.eighteenthstreet.user_service.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.eighteenthstreet.user_service.domain.model.User;
import com.eighteenthstreet.user_service.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
	private final JpaUserRepository jpaUserRepository;

	@Override
	public void save(User user) {
		jpaUserRepository.save(user);
	}

	@Override
	public boolean isExistUsername(String username) {
		return jpaUserRepository.findByUsername(username) != null;
	}

	@Override
	public Page<User> findActiveUsers(String name, Pageable pageable) {
		return jpaUserRepository.findActiveUsers(name, pageable);
	}

	@Override
	public User findByUsername(String username) {
		return jpaUserRepository.findByUsername(username);
	}

	@Override
	public User findByUserId(UUID userId) {
		return jpaUserRepository.findByUserId(userId);
	}
}
