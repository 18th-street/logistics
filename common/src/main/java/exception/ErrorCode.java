package exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
	// 클라이언트 오류
	INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.", "U001"),
	UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "인증되지 않은 접근입니다.", "U002"),
	FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "접근이 금지되었습니다.", "U003"),

	// Product
	PRODUCT_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 등록된 상품입니다.", "P001"),
	PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않은 상품입니다.", "P002");

	private final HttpStatus httpStatus;
	private final String message;
	private final String code;
}
