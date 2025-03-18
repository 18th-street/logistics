package com.eighteenthstreet.user_service.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequestDto(
	@NotBlank(message = "빈 값이 될 수 없습니다. 비밀번호를 입력해주세요.")
	@Size(min = 8, max = 15, message = "비밀번호는 8글자 이상 15글자 이하로 구성되어야 합니다.")
	@Pattern(
		regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+{}\\[\\]:;<>,.?/~\\-]).{8,15}$",
		message = "비밀번호는 최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자로 구성되어야 합니다."
	)
	String password
) {
}