package com.eighteenthstreet.user_service.presentation.dto;

import com.eighteenthstreet.user_service.domain.model.Role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignUpRequestDto(
	@NotBlank(message = "빈 값이 될 수 없습니다. 사용할 아이디를 입력해주세요.")
	@Pattern(regexp = "^[a-z0-9]{4,10}$", message = "아이디는 최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)로 구성되어야 합니다.")
	String username,

	@NotBlank(message = "빈 값이 될 수 없습니다. 비밀번호를 입력해주세요.")
	@Size(min = 8, max = 15, message = "비밀번호는 8글자 이상 15글자 이하로 구성되어야 합니다.")
	@Pattern(
		regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+{}\\[\\]:;<>,.?/~\\-]).{8,15}$",
		message = "비밀번호는 최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자로 구성되어야 합니다."
	)
	String password,

	@NotBlank(message = "빈 값이 될 수 없습니다. 슬랙 아이디를 입력해주세요.")
	@Size(max = 30, message = "슬랙 아이디는 30글자 이하로 구성되어야 합니다.")
	String slackId,

	@NotBlank(message = "빈 값이 될 수 없습니다. 이름을 입력해주세요.")
	String name,

	@NotBlank(message = "빈 값이 될 수 없습니다. 전화번호를 입력해주세요.")
	@Pattern(regexp = "^\\d{10,11}$", message = "전화번호는 공백이나 기호(-) 없이 10~11자리 숫자로 입력해주세요")
	String phone,

	Role role
) {
}
