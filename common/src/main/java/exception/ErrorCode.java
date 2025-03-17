package exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    // 클라이언트 오류
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.", "U001"),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "인증되지 않은 접근입니다.", "U002"),
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "접근이 금지되었습니다.", "U003"),

    // 허브 오류
    HUB_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 허브입니다.", "H001"),
    HUB_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 존재하는 허브입니다.", "H002");

    private final HttpStatus httpStatus;
    private final String message;
    private final String code;
}
