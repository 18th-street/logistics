package com.eighteenthstreet.user_service.presentation.controller;

import org.springframework.context.annotation.Description;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eighteenthstreet.user_service.application.dto.TokenDto;
import com.eighteenthstreet.user_service.application.dto.UserResponseDto;
import com.eighteenthstreet.user_service.application.service.UserService;
import com.eighteenthstreet.user_service.domain.model.Role;
import com.eighteenthstreet.user_service.presentation.dto.ChangePasswordRequestDto;
import com.eighteenthstreet.user_service.presentation.dto.SignInRequestDto;
import com.eighteenthstreet.user_service.presentation.dto.SignUpRequestDto;
import com.eighteenthstreet.user_service.presentation.dto.UpdateStatusRequestDto;
import com.eighteenthstreet.user_service.presentation.dto.UpdateUserRequestDto;
import com.eighteenthstreet.user_service.presentation.exceptions.BusinessException;

import exception.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
	private final UserService userService;

	@Description(
		"회원가입"
	)
	@PostMapping("/signUp")
	public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequestDto request) {
		if (userService.isExistUsername(request.username())) {
			throw new BusinessException(ErrorCode.DUPLICATE_USERNAME);
		}
		userService.signUp(request);
		return ResponseEntity.ok().build();
	}

	@Description(
		"로그인"
	)
	@PostMapping("/signIn")
	public ResponseEntity<TokenDto> signIn(@Valid @RequestBody SignInRequestDto request) {
		return ResponseEntity.ok(userService.signIn(request));
	}

	@Description(
		"로그아웃"
	)
	@PostMapping("/signOut")
	public ResponseEntity<Void> signOut(@RequestHeader("Authorization") String authorization) {
		userService.signOut(authorization);
		return ResponseEntity.ok().build();
	}

	@Description(
		"권한 조회"
	)
	@PreAuthorize("hasRole('MASTER')")
	public ResponseEntity<Role> getUserRole(@PathVariable("userId") Long userId) {
		Role role = userService.getUserRole(userId);
		return ResponseEntity.ok(role);
	}

	@Description(
		"사용자 정보 전체 조회 (마스터용)"
	)
	@GetMapping
	@PreAuthorize("hasRole('MASTER')")
	public ResponseEntity<Page<UserResponseDto>> getAllUsers(Pageable pageable) {
		return ResponseEntity.ok(userService.getAllUsers(pageable));
	}

	@Description(
		"사용자 정보 검색 (마스터용)"
	)
	@GetMapping("/search")
	@PreAuthorize("hasRole('MASTER')")
	public ResponseEntity<Page<UserResponseDto>> searchUsers(
		@RequestParam(name = "name", defaultValue = "") String name,
		@RequestParam(name = "size", defaultValue = "10") int size,
		@RequestParam(name = "sort", defaultValue = "createdAt") String sortField,
		@RequestParam(name = "direction", defaultValue = "DESC") String direction,
		Pageable pageable
	) {
		if (size != 10 && size != 30 && size != 50) {
			size = 10;
		}

		Sort.Direction sortDirection = direction.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;

		Pageable customPageable = PageRequest.of(
			pageable.getPageNumber(),
			size,
			Sort.by(sortDirection, sortField)
		);
		return ResponseEntity.ok(userService.searchUsers(name, customPageable));
	}

	@Description(
		"사용자 정보 상세 조회"
	)
	@GetMapping("/{userId}")
	public ResponseEntity<UserResponseDto> getUserDetail(@PathVariable("userId") Long userId) {
		return ResponseEntity.ok(userService.getUserDetail(userId));
	}

	@Description(
		"사용자 정보 수정 (마스터용)"
	)
	@PatchMapping("/{userId}")
	@PreAuthorize("hasRole('MASTER')")
	public ResponseEntity<UserResponseDto> updateUserInfo(
		@PathVariable("userId") Long userId,
		@Valid @RequestBody UpdateUserRequestDto request
	) {
		return ResponseEntity.ok(userService.updateUserInfo(userId, request));
	}

	@Description(
		"사용자 비밀번호 수정 (마스터용)"
	)
	@PatchMapping("/{userId}/change-password")
	@PreAuthorize("hasRole('MASTER')")
	public ResponseEntity<Void> changePassword(
		@PathVariable("userId") Long userId,
		@Valid @RequestBody ChangePasswordRequestDto request
	) {
		userService.changePassword(userId, request);
		return ResponseEntity.ok().build();
	}

	@Description(
		"상태 변경 (WAITING -> COMPLETE)"
	)
	@PatchMapping("/update/status")
	public ResponseEntity<Void> updateStatus(@RequestBody UpdateStatusRequestDto request) {
		userService.updateStatus(request);
		return ResponseEntity.ok().build();
	}

	@Description(
		"사용자 정보 삭제"
	)
	@DeleteMapping("/{userId}")
	@PreAuthorize("hasRole('MASTER')")
	public ResponseEntity<Void> deleteUser(@PathVariable("userId") Long userId) {
		userService.deleteUser(userId);
		return ResponseEntity.ok().build();
	}
}
