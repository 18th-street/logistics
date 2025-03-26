package com.eighteenthstreet.user_service.application.service;

import java.util.UUID;
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
import com.eighteenthstreet.user_service.domain.model.Status;
import com.eighteenthstreet.user_service.domain.model.User;
import com.eighteenthstreet.user_service.domain.repository.UserRepository;
import com.eighteenthstreet.user_service.infrastructure.security.JwtProvider;
import com.eighteenthstreet.user_service.presentation.dto.ChangePasswordRequestDto;
import com.eighteenthstreet.user_service.presentation.dto.SignInRequestDto;
import com.eighteenthstreet.user_service.presentation.dto.SignUpRequestDto;
import com.eighteenthstreet.user_service.presentation.dto.UpdateStatusRequestDto;
import com.eighteenthstreet.user_service.presentation.dto.UpdateUserRequestDto;
import com.eighteenthstreet.user_service.presentation.exceptions.CustomException;

import auth.JwtUtil;
import auth.Role;
import exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
	private final RedisTemplate<String, String> redisTemplate;
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final JwtProvider jwtProvider;
	private final UserMapper userMapper;
	private final JwtUtil jwtUtil;

	@Value("${status-code}")
	private String statusCode;

	public Boolean validation(String username) {
		User user = userRepository.findByUsername(username);
		return user != null;
	}

	@Transactional
	public void signUp(SignUpRequestDto request) {
		User user = User.builder()
			.username(request.username())
			.password(passwordEncoder.encode(request.password()))
			.slackId(request.slackId())
			.name(request.name())
			.phone(request.phone())
			.email(request.email())
			.role(request.role())
			.status(Status.WAITING)
			.build();
		userRepository.save(user);
	}

	@Transactional
	public TokenDto signIn(SignInRequestDto request) {
		User user = userRepository.findByUsername(request.username());
		if (user == null) {
			throw new CustomException(ErrorCode.USER_NOT_FOUND);
		}
		if (!passwordEncoder.matches(request.password(), user.getPassword())) {
			throw new CustomException(ErrorCode.INVALID_PASSWORD);
		}
		String accessToken = jwtProvider.createAccessToken(user);
		String refreshToken = jwtProvider.createRefreshToken(user);

		// Redis에 토큰 저장 ( key : "refresh_token:<userId>" )
		redisTemplate.opsForValue()
			.set("refresh_token:" + user.getUserId(), refreshToken, jwtProvider.getRefreshExpiration(),
				TimeUnit.MILLISECONDS);

		return new TokenDto(accessToken, refreshToken);
	}

	@Transactional
	public void signOut(String authorization) {
		if (authorization == null || !authorization.startsWith("Bearer ")) {
			throw new CustomException(ErrorCode.INVALID_TOKEN);
		}
		String accessToken = authorization.replace("Bearer ", "");

		// 블랙리스트 추가
		redisTemplate.opsForValue()
			.set("blacklist:" + accessToken, "expired", jwtProvider.getAccessExpiration(), TimeUnit.SECONDS);

		// 기존 Redis 저장된 Refresh Token 삭제
		UUID userId = jwtUtil.getUserIdFromToken(authorization);
		redisTemplate.delete("refresh_token:" + userId);
	}

	public Role getUserRole(UUID userId) {
		User user = userRepository.findByUserId(userId);
		if (user == null) {
			throw new CustomException(ErrorCode.USER_NOT_FOUND);
		}
		return user.getRole();
	}

	public Page<UserResponseDto> getAllUsers(Pageable pageable) {
		return userRepository.findActiveUsers(null, pageable).map(userMapper::toUserResponseDto);
	}

	public Page<UserResponseDto> searchUsers(String name, Pageable pageable) {
		return userRepository.findActiveUsers(name, pageable).map(userMapper::toUserResponseDto);
	}

	public UserResponseDto getUserDetail(UUID userId) {
		User user = loginUser();
		if (!user.getRole().equals(Role.MASTER) && !user.getUserId().equals(userId)) {
			throw new CustomException(ErrorCode.ACCESS_DENIED);
		}

		User targetUser = userRepository.findByUserId(userId);
		if (targetUser == null) {
			throw new CustomException(ErrorCode.USER_NOT_FOUND);
		}
		return userMapper.toUserResponseDto(targetUser);
	}

	public UserResponseDto getUserDetailInternal(UUID userId) {
		User targetUser = userRepository.findByUserId(userId);
		if (targetUser == null) {
			throw new CustomException(ErrorCode.USER_NOT_FOUND);
		}
		return userMapper.toUserResponseDto(targetUser);
	}

	@Transactional
	public UserResponseDto updateUserInfo(UUID userId, UpdateUserRequestDto request) {
		User user = userRepository.findByUserId(userId);
		if (user == null) {
			throw new CustomException(ErrorCode.USER_NOT_FOUND);
		}
		user.update(request);
		return userMapper.toUserResponseDto(user);
	}

	@Transactional
	public void changePassword(UUID userId, ChangePasswordRequestDto request) {
		User user = userRepository.findByUserId(userId);
		if (user == null) {
			throw new CustomException(ErrorCode.USER_NOT_FOUND);
		}

		if (passwordEncoder.matches(request.password(), user.getPassword())) {
			throw new CustomException(ErrorCode.SAME_PASSWORD_NOT_ALLOWED);
		}
		String newPassword = passwordEncoder.encode(request.password());
		user.updatePassword(newPassword);
	}

	@Transactional
	public void updateStatus(UUID userId, UpdateStatusRequestDto request) {
		User user = userRepository.findByUserId(userId);
		if (user == null) {
			throw new CustomException(ErrorCode.USER_NOT_FOUND);
		}

		if (!statusCode.equals(request.code())) {
			throw new CustomException(ErrorCode.INVALID_AUTHENTICATION);
		}
		user.updateStatus();
	}

	@Transactional
	public void deleteUser(UUID userId) {
		User user = userRepository.findByUserId(userId);
		if (user == null) {
			throw new CustomException(ErrorCode.USER_NOT_FOUND);
		}
		user.performSoftDelete();
	}

	private User loginUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new CustomException(ErrorCode.AUTHENTICATION_REQUIRED);
		}
		return userRepository.findByUserId(UUID.fromString(authentication.getName()));
	}
}
