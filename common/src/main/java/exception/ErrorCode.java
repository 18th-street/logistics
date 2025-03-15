package exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	// 클라이언트 오류
	INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.", 4001),
	UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "인증되지 않은 접근입니다.", 4002),
	FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "접근이 금지되었습니다.", 4003),

	// 로그인 및 인증 관련 오류
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.", 4004),
	INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 올바르지 않습니다.", 4005),
	ACCOUNT_LOCKED(HttpStatus.FORBIDDEN, "계정이 잠겨 있습니다.", 4006),
	ACCOUNT_DISABLED(HttpStatus.FORBIDDEN, "비활성화된 계정입니다.", 4007),
	DUPLICATE_USERNAME(HttpStatus.CONFLICT, "이미 존재하는 아이디입니다.", 4008),

	INVALID_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "인증을 잘못했습니다.", 4009),
	INVALID_AUTH_CODE(HttpStatus.UNAUTHORIZED, "잘못된 인증 코드입니다.", 4010),
	AUTHENTICATION_REQUIRED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다.", 4011),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.", 4012),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.", 4013),
	SAME_PASSWORD_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "이전과 동일한 비밀번호로 변경할 수 없습니다.", 4014),
	DELETED_INFORMATION(HttpStatus.BAD_REQUEST, "삭제된 정보입니다.", 4015),
	ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.", 4016);

	private final HttpStatus httpStatus;
	private final String message;
	private final Integer code;

	ErrorCode(HttpStatus httpStatus, String message, int code) {
		this.httpStatus = httpStatus;
		this.message = message;
		this.code = code;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public String getMessage() {
		return message;
	}

	public int getCode() {
		return code;
	}
}
