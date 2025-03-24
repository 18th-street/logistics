package com.eighteenthstreet.user_service.presentation.controller;

import java.util.UUID;

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
import com.eighteenthstreet.user_service.presentation.dto.ChangePasswordRequestDto;
import com.eighteenthstreet.user_service.presentation.dto.SignInRequestDto;
import com.eighteenthstreet.user_service.presentation.dto.SignUpRequestDto;
import com.eighteenthstreet.user_service.presentation.dto.UpdateStatusRequestDto;
import com.eighteenthstreet.user_service.presentation.dto.UpdateUserRequestDto;
import com.eighteenthstreet.user_service.presentation.exceptions.CustomException;

import auth.Role;
import exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "User-Controller", description = "User CRUD API 엔트포인트")
public class UserController {
	private final UserService userService;

	@Operation(summary = "사용자 유효성 검증", description = "username에 해당하는 사용자의 존재 여부를 반환합니다.")
	@ApiResponse(responseCode = "200", description = "검증 성공", content = @Content(schema = @Schema(implementation = Boolean.class)))
	@GetMapping("/valid")
	public ResponseEntity<Boolean> validation(@RequestParam String username) {
		return ResponseEntity.ok(userService.validation(username));
	}

	@Operation(summary = "회원가입", description = "신규 사용자 계정을 생성합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "회원가입 성공"),
		@ApiResponse(responseCode = "409", description = "이미 존재하는 사용자명")
	})
	@PostMapping("/signUp")
	public ResponseEntity<Void> signUp(
		@Valid @RequestBody @Parameter(description = "회원가입 요청 데이터") SignUpRequestDto request) {
		if (userService.validation(request.username())) {
			throw new CustomException(ErrorCode.DUPLICATE_USERNAME);
		}
		userService.signUp(request);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "로그인", description = "사용자 로그인 및 토큰 발급")
	@PostMapping("/signIn")
	public ResponseEntity<TokenDto> signIn(@Valid @RequestBody SignInRequestDto request) {
		return ResponseEntity.ok(userService.signIn(request));
	}

	@Operation(summary = "로그아웃", description = "액세스 토큰을 통해 로그아웃 처리")
	@PostMapping("/signOut")
	public ResponseEntity<Void> signOut(@RequestHeader("Authorization") String authorization) {
		userService.signOut(authorization);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "권한 조회", description = "마스터 권한으로 특정 사용자의 권한을 조회합니다.")
	@GetMapping("/{userId}/role")
	@PreAuthorize("hasRole('MASTER')")
	public ResponseEntity<Role> getUserRole(@PathVariable("userId") UUID userId) {
		Role role = userService.getUserRole(userId);
		return ResponseEntity.ok(role);
	}

	@Operation(summary = "사용자 전체 조회", description = "마스터 권한으로 모든 사용자 정보를 조회합니다.")
	@GetMapping
	@PreAuthorize("hasRole('MASTER')")
	public ResponseEntity<Page<UserResponseDto>> getAllUsers(Pageable pageable) {
		return ResponseEntity.ok(userService.getAllUsers(pageable));
	}

	@Operation(summary = "사용자 검색", description = "마스터 권한으로 사용자 이름 기준 검색을 수행합니다.")
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

	@Operation(summary = "사용자 상세 조회", description = "사용자 ID로 특정 사용자 정보를 조회합니다. 본인 또는 마스터 권한 필요.")
	@GetMapping("/{userId}")
	public ResponseEntity<UserResponseDto> getUserDetail(@PathVariable("userId") UUID userId) {
		return ResponseEntity.ok(userService.getUserDetail(userId));
	}

	@Operation(summary = "사용자 정보 수정", description = "마스터 권한으로 사용자 정보를 수정합니다.")
	@PatchMapping("/{userId}")
	@PreAuthorize("hasRole('MASTER')")
	public ResponseEntity<UserResponseDto> updateUserInfo(
		@PathVariable("userId") UUID userId,
		@Valid @RequestBody UpdateUserRequestDto request
	) {
		return ResponseEntity.ok(userService.updateUserInfo(userId, request));
	}

	@Operation(summary = "비밀번호 수정", description = "마스터 권한으로 사용자의 비밀번호를 수정합니다.")
	@PatchMapping("/{userId}/change-password")
	@PreAuthorize("hasRole('MASTER')")
	public ResponseEntity<Void> changePassword(
		@PathVariable("userId") UUID userId,
		@Valid @RequestBody ChangePasswordRequestDto request
	) {
		userService.changePassword(userId, request);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "사용자 상태 변경", description = "마스터 권한으로 사용자 상태를 WAITING에서 COMPLETE로 변경합니다.")
	@PatchMapping("/{userId}/update-status")
	@PreAuthorize("hasRole('MASTER')")
	public ResponseEntity<Void> updateStatus(
		@PathVariable("userId") UUID userId,
		@RequestBody UpdateStatusRequestDto request
	) {
		userService.updateStatus(userId, request);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "사용자 삭제", description = "마스터 권한으로 사용자 정보를 삭제합니다.")
	@DeleteMapping("/{userId}")
	@PreAuthorize("hasRole('MASTER')")
	public ResponseEntity<Void> deleteUser(@PathVariable("userId") UUID userId) {
		userService.deleteUser(userId);
		return ResponseEntity.ok().build();
	}
}