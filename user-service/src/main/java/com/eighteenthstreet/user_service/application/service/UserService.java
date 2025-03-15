package com.eighteenthstreet.user_service.application.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eighteenthstreet.user_service.application.dto.TokenDto;
import com.eighteenthstreet.user_service.application.dto.UserResponseDto;
import com.eighteenthstreet.user_service.domain.model.Role;
import com.eighteenthstreet.user_service.domain.model.Status;
import com.eighteenthstreet.user_service.domain.model.User;
import com.eighteenthstreet.user_service.domain.repository.UserRepository;
import com.eighteenthstreet.user_service.infrastructure.security.JwtUtil;
import com.eighteenthstreet.user_service.presentation.dto.ChangePasswordRequestDto;
import com.eighteenthstreet.user_service.presentation.dto.SignInRequestDto;
import com.eighteenthstreet.user_service.presentation.dto.SignUpRequestDto;
import com.eighteenthstreet.user_service.presentation.dto.UpdateStatusRequestDto;
import com.eighteenthstreet.user_service.presentation.dto.UpdateUserRequestDto;
import com.eighteenthstreet.user_service.presentation.exceptions.BusinessException;

import exception.ErrorCode;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final RedisTemplate<String, String> redisTemplate;
	private final JwtUtil jwtUtil;
	private final UserMapper userMapper;

	@Value("${status-code}")
	private String statusCode;

	public boolean isExistUsername(String username) {
		return userRepository.isExistUsername(username);
	}

	@Transactional
	public void signUp(SignUpRequestDto request) {
		User user = User.builder()
			.username(request.username())
			.password(passwordEncoder.encode(request.password()))
			.slackId(request.slackId())
			.name(request.name())
			.phone(request.phone())
			.role(request.role())
			.status(Status.WAITING)
			.deletedBy(null)
			.deletedAt(null)
			.build();
		userRepository.save(user);
	}

	@Transactional
	public TokenDto signIn(SignInRequestDto request) {
		User user = userRepository.findByUsername(request.username());
		if (user == null) {
			throw new BusinessException(ErrorCode.USER_NOT_FOUND);
		}
		if (!passwordEncoder.matches(request.password(), user.getPassword())) {
			throw new BusinessException(ErrorCode.INVALID_PASSWORD);
		}
		String accessToken = jwtUtil.createAccessToken(user);
		String refreshToken = jwtUtil.createRefreshToken(user);
		return new TokenDto(accessToken, refreshToken);
	}

	@Transactional
	public void signOut(String authorization) {
		if (authorization == null || !authorization.startsWith("Bearer ")) {
			throw new BusinessException(ErrorCode.INVALID_TOKEN);
		}
		String accessToken = authorization.replace("Bearer ", "");

		// 블랙리스트 추가
		Claims claims = jwtUtil.getClaims(accessToken);
		Long userId = Long.valueOf(claims.getSubject());

		Long expirationTimeMillis = claims.getExpiration().getTime(); // 만료 시간 (밀리초)
		Long ttl = (expirationTimeMillis - System.currentTimeMillis()) / 1000; // Redis TTL (초 단위)

		redisTemplate.opsForValue().set("blacklist:" + accessToken, "expired", ttl, TimeUnit.SECONDS);

		// 기존 Redis 저장된 Refresh Token 삭제
		redisTemplate.delete("refresh:" + userId);
	}

	public Role getUserRole(Long userId) {
		return userRepository.findById(userId).getRole();
	}

	public Page<UserResponseDto> getAllUsers(Pageable pageable) {
		return userRepository.findActiveUsers(null, pageable).map(userMapper::toUserResponseDto);
	}

	public Page<UserResponseDto> searchUsers(String name, Pageable pageable) {
		return userRepository.findActiveUsers(name, pageable).map(userMapper::toUserResponseDto);
	}

	public UserResponseDto getUserDetail(Long userId) {
		User user = loginUser();
		if (!user.getRole().equals(Role.MASTER) && !user.getUserId().equals(userId)) {
			throw new BusinessException(ErrorCode.UNAUTHORIZED_ACCESS);
		}

		User targetUser = userRepository.findById(userId);
		if (targetUser.getDeletedAt() != null && targetUser.getDeletedBy() != null) {
			throw new BusinessException(ErrorCode.DELETED_INFORMATION);
		}
		return userMapper.toUserResponseDto(targetUser);
	}

	@Transactional
	public UserResponseDto updateUserInfo(Long userId, UpdateUserRequestDto request) {
		User user = userRepository.findById(userId);
		user.update(request);
		return userMapper.toUserResponseDto(user);
	}

	@Transactional
	public void changePassword(Long userId, ChangePasswordRequestDto request) {
		User user = userRepository.findById(userId);
		if (passwordEncoder.matches(request.password(), user.getPassword())) {
			throw new BusinessException(ErrorCode.SAME_PASSWORD_NOT_ALLOWED);
		}
		String newPassword = passwordEncoder.encode(request.password());
		user.updatePassword(newPassword);
	}

	@Transactional
	public void updateStatus(UpdateStatusRequestDto request) {
		User user = loginUser();
		if (!statusCode.equals(request.code())) {
			throw new BusinessException(ErrorCode.INVALID_AUTHENTICATION);
		}
		user.updateStatus();
	}

	@Transactional
	public void deleteUser(Long userId) {
		User loginUser = loginUser();
		User targetUser = userRepository.findById(userId);
		targetUser.delete(loginUser);
	}

	private User loginUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		}
		Long userId = Long.valueOf(authentication.getName());
		return userRepository.findById(userId);
	}
}
