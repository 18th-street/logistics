package exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
	// 클라이언트 오류
	INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.", 4001),
	UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "인증되지 않은 접근입니다.", 4002),
	FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "접근이 금지되었습니다.", 4003),

	// Company
	COMPANY_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 등록되어 있는 업체입니다.", 5000),
	COMPANY_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당하는 업체를 찾을 수 없습니다.", 5001),

	// Product
	PRODUCT_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 등록된 상품입니다.", 6000),
	PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않은 상품입니다.", 6001),

	// Order
	ORDER_NOT_FOUND(HttpStatus.BAD_REQUEST, "주문을 찾을 수 없습니다.", 7000);

	private final HttpStatus httpStatus;
	private final String message;
	private final Integer code;
}
