package com.eighteenthstreet.user_service;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.eighteenthstreet.user_service.application.service.UserService;
import com.eighteenthstreet.user_service.domain.repository.UserRepository;

@ExtendWith(MockitoExtension.class)  // ✅ Mockito 사용
class UserServiceTest {

	@InjectMocks
	private UserService userService;  // ✅ 테스트할 대상

	@Mock
	private UserRepository userRepository;  // ✅ Mock 객체

	@Mock
	private BCryptPasswordEncoder passwordEncoder;  // ✅ 비밀번호 인코더 Mock

	@BeforeEach
	void setUp() {
		when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
	}

	// @Test
	// void givenValidSignUpRequest_whenRegisteringUser_thenUserIsSavedSuccessfully() {
	// 	// ✅ Given (테스트 데이터 준비)
	// 	SignUpRequestDto request = SignUpRequestDto.builder()
	// 		.username("testuser1")
	// 		.password("Abscd123!")
	// 		.slackId("abc@naver.com")
	// 		.name("홍길동")
	// 		.phone("01011112222")
	// 		.role(Role.HUB)
	// 		.build();
	//
	// 	// ✅ When & Then (예외 없이 실행되는지 검증)
	// 	assertThatCode(() -> userService.signUp(request))
	// 		.doesNotThrowAnyException();  // 예외가 발생하지 않는지 확인
	//
	// 	// ✅ Verify (저장이 실제로 호출되었는지 검증)
	// 	verify(userRepository, times(1)).save(any(User.class));
	// }
}
