package exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
	// 클라이언트 오류
	INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.", "4001"),
	UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "인증되지 않은 접근입니다.", "4002"),
	FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "접근이 금지되었습니다.", "4003"),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.", "4004"),

	SLACK_SEND_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "슬랙 메시지 전송이 실패했습니다.", "S001");

	private final HttpStatus httpStatus;
	private final String message;
	private final String code;
}
