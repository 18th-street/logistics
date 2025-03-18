package exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
	// 로그인 및 인증 관련 오류
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.", "U001"),
	DELETED_USER(HttpStatus.BAD_REQUEST, "삭제된 사용자 정보입니다.", "U002"),
	DUPLICATE_USERNAME(HttpStatus.CONFLICT, "이미 존재하는 아이디입니다.", "U003"),
	INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 올바르지 않습니다.", "U004"),
	SAME_PASSWORD_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "이전과 동일한 비밀번호로 변경할 수 없습니다.", "U005"),
	INVALID_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증입니다.", "U006"),
	AUTHENTICATION_REQUIRED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다.", "U007"),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.", "U008"),
	ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.", "U009"),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.", "U010");

	// Company
	COMPANY_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 등록되어 있는 업체입니다.", "C001"),
	COMPANY_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당하는 업체를 찾을 수 없습니다.", "C002");

	private final HttpStatus httpStatus;
	private final String message;
	private final String code;
}
