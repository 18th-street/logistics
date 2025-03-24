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
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.", "U010"),

	// Company
	COMPANY_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 등록되어 있는 업체입니다.", "C001"),
	COMPANY_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당하는 업체를 찾을 수 없습니다.", "C002"),
	COMPANY_UPDATE_ROLE_DENIED(HttpStatus.FORBIDDEN, "권한이 MASTER 또는 HUB 또는 COMPANY인 사용자여야 합니다.", "C003"),
	COMPANY_POST_ROLE_DENIED(HttpStatus.FORBIDDEN, "권한이 MASTER 또는 HUB인 사용자여야 합니다.", "C004"),
	COMPANY_HUB_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 업체가 소속된 Hub를 찾을 수 없습니다.", "C005"),

	// Product
	PRODUCT_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 등록된 상품입니다.", "P001"),
	PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않은 상품입니다.", "P002"),
	PRODUCT_NOT_FOUND_COMPANY(HttpStatus.BAD_REQUEST, "업체 조회 API 호출을 실패했습니다.", "P006"),
	PRODUCT_MISMATCH_HUB_ID(HttpStatus.BAD_REQUEST, "요청 허브 ID와 응답 허브 ID가 다릅니다.", "P007"),
	PRODUCT_BULK_NOT_FOUND(HttpStatus.BAD_REQUEST, "상품 정보 Bulk 조회를 실패했습니다.", "P003"),

	// Order
	ORDER_NOT_FOUND(HttpStatus.BAD_REQUEST, "주문을 찾을 수 없습니다.", "O001"),
	ORDER_COMPANY_NOT_SUPPLIER(HttpStatus.BAD_REQUEST, "주문 내역에 있는 생산 업체가 아닙니다.", "O002"),
	ORDER_COMPANY_NOT_RECEIVER(HttpStatus.BAD_REQUEST, "주문 내역에 있는 공급 업체가 아닙니다.", "O003"),
	ORDER_PRODUCT_NOT_FOR_SALE(HttpStatus.BAD_REQUEST, "주문 내역에 있는 상품은 현재 판매 중인 상품이 아닙니다.", "O004"),
	ORDER_INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "재고가 부족합니다. 주문 상품 수량을 변경해주세요.", "O005"),
	ORDER_STATUS_UPDATE_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "배송 전 상태인 주문만 수정할 수 있습니다.", "O006"),
	ORDER_DELETE_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "배송 전 상태인 주문만 삭제할 수 있습니다.", "O007"),
	ORDER_CANCEL_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "주문 실패 또는 배송 생성 실패인 주문은 취소할 수 없습니다.", "O008"),
	ORDER_UPDATE_ROLE_DENIED(HttpStatus.FORBIDDEN, "권한이 MASTER 또는 HUB인 사용자만 주문을 수정할 수 있습니다.", "O009"),
	ORDER_DELETE_ROLE_DENIED(HttpStatus.FORBIDDEN, "권한이 MASTER 또는 HUB인 사용자만 주문을 삭제할 수 있습니다.", "O010"),
	ORDER_CANCEL_ROLE_DENIED(HttpStatus.FORBIDDEN, "권한이 MASTER 또는 HUB인 사용자만 주문을 취소할 수 있습니다.", "O011"),
	ORDER_PRODUCT_DECREASE_STOCK_FAIL(HttpStatus.BAD_REQUEST, "OrderService에서 상품 재고 차감 API 호출을 실패하였습니다.", "O012"),
	ORDER_ITEM_NOT_FOUND(HttpStatus.BAD_REQUEST, "주문 상품 목록을 찾을 수 없습니다.", "O013"),
	ORDER_COMPANY_GET_API_FAIL(HttpStatus.BAD_REQUEST, "OrderService에서 업체 조회 API 호출을 실패하였습니다.", "O014"),
	ORDER_PRODUCT_RESTORE_STOCK_FAIL(HttpStatus.BAD_REQUEST, "OrderService에서 상품 재고 복원 API 호출을 실패하였습니다", "O015"),
	ORDER_DELIVERYID_MISTMATCH(HttpStatus.BAD_REQUEST, "해당 상품의 배송 ID와 일치하지 않습니다.", "O016"),

	// 배달 서비스
	DELIVERY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 배달을 찾을 수 없습니다.", "D001"),
	INVALID_DELIVERY(HttpStatus.NOT_FOUND, "해당 배달은 배송중이라 취소를 할 수 없습니다.", "D002"),

	// 배달 경로 서비스
	DELIVERY_ROUTE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 배달경로을 찾을 수 없습니다.", "DR001"),
	DELIVERY_ROUTE_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "배송 경로 생성에 실패했습니다.", "DR002"),
	DELIVERY_ROUTE_NOT_FOUND_BY_ID(HttpStatus.NOT_FOUND, "해당 Delivery ID로 배달 경로를 찾을 수 없습니다.", "DR003"),
	DELIVERY_ROUTE_DELETION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "배달 경로 삭제에 실패했습니다.", "DR004"),

	// 배달 담당자 서비스
	DELIVERY_AGENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 배달담당자를 찾을 수 없습니다.", "D001"),
	INVALID_DELIVERY_AGENT_STATUS(HttpStatus.BAD_REQUEST, "배달 담당자가 배송 중이므로 삭제할 수 없습니다.", "D002"),
	INVALID_DELIVERY_AGENT(HttpStatus.BAD_REQUEST, "현재 남은 배달 담당자가 없습니다.", "D003"),
	DELIVERY_AGENT_NOT_FOUND_BY_ID(HttpStatus.NOT_FOUND, "해당 Delivery ID로 배달 담당자를 찾을 수 없습니다.", "D004"),
	DELIVERY_AGENT_DELETION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "배달 담당자 삭제에 실패했습니다.", "D005"),
	INVALID_HUB_ID(HttpStatus.BAD_REQUEST, "허브 ID가 유효하지 않습니다.", "D006"),
	MISSING_HUB_ID(HttpStatus.BAD_REQUEST, "허브 담당자는 허브 ID가 필요합니다.", "D007"),
	HUB_MISMATCH(HttpStatus.BAD_REQUEST, "허브 담당자의 소속 허브와 경로 허브가 일치하지 않습니다.", "D008"),

	// 허브 오류
	HUB_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 허브입니다.", "H001"),
	HUB_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 존재하는 허브입니다.", "H002"),

	// 허브 경로 오류
	HUB_ROUTE_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 경로입니다.", "H003"),
	HUB_ROUTE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 경로입니다.", "H004"),

	// 슬랙 오류
	SLACK_SEND_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "슬랙 메시지 전송을 실패했습니다.", "S001"),
	SLACK_ID_EXTRACT_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "슬랙 멤버 ID 추출을 실패했습니다.", "S002"),
	SLACK_TRANSACTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "슬랙 메시지 저장을 실패했습니다.", "S003"),
	SLACK_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 슬랙 메시지 정보를 찾을 수 없습니다.", "S004"),
	ORDER_GET_API_FAIL(HttpStatus.BAD_REQUEST, "주문 조회 API 호출을 실패했습니다.", "S005"),
	DELIVERY_GET_API_FAIL(HttpStatus.BAD_REQUEST, "배달 조회 API 호출을 실패했습니다.", "S006"),
	HUB_GET_API_FAIL(HttpStatus.BAD_REQUEST, "허브 조회 API 호출을 실패했습니다.", "S007"),
	USER_GET_API_FAIL(HttpStatus.BAD_REQUEST, "유저 조회 API 호출을 실패했습니다.", "S008");
	
	private final HttpStatus httpStatus;
	private final String message;
	private final String code;
}
