package exception;


import org.springframework.http.HttpStatus;


public enum ErrorCode {
//    // 클라이언트 오류
//    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.", 4001),
//    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "인증되지 않은 접근입니다.", 4002),
//    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "접근이 금지되었습니다.", 4003);

    // 배달 서비스
    DELIVERY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 배달을 찾을 수 없습니다.", "D001"),
    INVALID_DELIVERY(HttpStatus.NOT_FOUND, "해당 배달은 취소를 할 수 없습니다.", "D002"),

    // 배달 경로 서비스
    DELIVERY_ROUTE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 배달경로을 찾을 수 없습니다.", "DR001"),

    // 배달 담당자 서비스
    DELIVERY_AGENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 배달담당자를 찾을 수 없습니다.", "D001"),
    INVALID_DELIVERY_AGENT_STATUS(HttpStatus.BAD_REQUEST, "배달 담당자가 배송 중이므로 삭제할 수 없습니다.", "D002");


    private final HttpStatus httpStatus;
    private final String message;
    private final String code;

    ErrorCode(HttpStatus status, String code, String message) {
        this.httpStatus = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
