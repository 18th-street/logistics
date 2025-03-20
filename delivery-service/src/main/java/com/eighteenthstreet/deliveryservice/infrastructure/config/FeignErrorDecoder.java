package com.eighteenthstreet.deliveryservice.infrastructure.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.eighteenthstreet.deliveryservice.presentation.exception.error.CustomFeignException;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.Response;
import feign.codec.ErrorDecoder;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

	private final ObjectMapper objectMapper = new ObjectMapper();

	public Exception decode(String methodKey, Response response) {
		try (InputStream bodyIs = response.body().asInputStream()) {
			// 서버 응답을 Map으로 파싱
			Map<String, String> errorResponse = objectMapper.readValue(bodyIs, Map.class);
			String code = errorResponse.getOrDefault("code", "UNKNOWN_ERROR");
			String message = errorResponse.getOrDefault("message", "알 수 없는 오류가 발생했습니다.");
			return new CustomFeignException(code, message);
		} catch (IOException e) {
			return new CustomFeignException("Feign 호출 중 오류: " + response.status());
		}
	}
}