package com.eighteenthstreet.user_service.presentation.dto;

import com.eighteenthstreet.user_service.domain.model.Status;

import auth.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserRequestDto(
	@NotBlank(message = "이메일을 입력해주세요.")
	@Email(message = "유효한 이메일 형식이 아닙니다.")
	@Size(max = 50, message = "이메일은 최대 50자까지 입력할 수 있습니다.")
	String email,

	@NotBlank(message = "빈 값이 될 수 없습니다. 이름을 입력해주세요.")
	String name,

	@NotBlank(message = "빈 값이 될 수 없습니다. 전화번호를 입력해주세요.")
	@Pattern(regexp = "^\\d{10,11}$", message = "전화번호는 공백이나 기호 없이 10~11자리 숫자로 입력해주세요")
	String phone,

	@NotBlank(message = "빈 값이 될 수 없습니다. 슬랙 아이디를 입력해주세요.")
	@Size(max = 30, message = "슬랙 아이디는 30글자 이하로 구성되어야 합니다.")
	String slackId,
	Role role,
	Status status
) {
}
