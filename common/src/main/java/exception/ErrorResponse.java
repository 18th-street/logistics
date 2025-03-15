package exception;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ErrorResponse {
	private Integer code;
	private String message;

	public ErrorResponse(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public ErrorResponse(ErrorCode errorCode) {
		this.code = errorCode.getCode();
		this.message = errorCode.getMessage();
	}

	public Integer getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
