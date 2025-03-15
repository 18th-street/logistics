package com.eighteenthstreet.user_service.infrastructure.repository;

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
	private final UserJpaRepository userJpaRepository;

	@Override
	public void save(User user) {
		userJpaRepository.save(user);
	}

	@Override
	public boolean isExistUsername(String username) {
		return userJpaRepository.findByUsername(username) != null;
	}

	@Override
	public Page<User> findActiveUsers(String name, Pageable pageable) {
		return userJpaRepository.findActiveUsers(name, pageable);
	}

	@Override
	public User findByUsername(String username) {
		return userJpaRepository.findByUsername(username);
	}

	@Override
	public User findById(Long userId) {
		return userJpaRepository.findById(userId).orElse(null);
	}
}
