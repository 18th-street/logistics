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

	// Company
	COMPANY_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 등록되어 있는 업체입니다.", "C001"),
	COMPANY_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당하는 업체를 찾을 수 없습니다.", "C002"),

	// Product
	PRODUCT_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 등록된 상품입니다.", "P001"),
	PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않은 상품입니다.", "P002"),

	// Order
	ORDER_NOT_FOUND(HttpStatus.BAD_REQUEST, "주문을 찾을 수 없습니다.", "O001"),
	ORDER_COMPANY_NOT_SUPPLIER(HttpStatus.BAD_REQUEST, "주문 내역에 있는 생산 업체가 아닙니다.", "O002"),
	ORDER_COMPANY_NOT_RECEIVER(HttpStatus.BAD_REQUEST, "주문 내역에 있는 공급 업체가 아닙니다.", "O003"),
	ORDER_PRODUCT_NOT_FOR_SALE(HttpStatus.BAD_REQUEST, "주문 내역에 있는 상품은 현재 판매 중인 상품이 아닙니다.", "O004"),
	ORDER_INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "재고가 부족합니다. 주문 상품 수량을 변경해주세요.", "O005"),
	ORDER_STATUS_UPDATE_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "배송 전 상태인 주문만 수정할 수 있습니다.", "O006"),
	ORDER_DELETE_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "배송 전 상태인 주문만 삭제할 수 있습니다.", "O007"),
	ORDER_CANCEL_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "주문 실패 또는 배송 생성 실패인 주문은 취소할 수 없습니다.", "O008");

	private final HttpStatus httpStatus;
	private final String message;
	private final String code;
}
