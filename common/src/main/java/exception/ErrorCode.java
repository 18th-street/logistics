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

	SLACK_SEND_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "슬랙 메시지 전송을 실패했습니다.", "S001"),
	EXTRACT_ID_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "슬랙 멤버 ID 추출을 실패했습니다.", "S002"),
	SL_TRANSACTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "슬랙 메시지 저장을 실패했습니다.", "S003"),
	SLACK_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 슬랙 메시지 정보를 찾을 수 없습니다.", "S004");

	private final HttpStatus httpStatus;
	private final String message;
	private final String code;
}
