package com.eighteenthstreet.slack_service.infrastructure.config;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.eighteenthstreet.slack_service.presentation.exception.CustomException;

import exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class ServletRequestUtil {

	public String getAccessTokenFromHeader() {
		// 현재 요청에서 HttpServletRequest 가져오기
		ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		if (attributes == null) {
			throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
		HttpServletRequest request = attributes.getRequest();
		return request.getHeader("Authorization");
	}
}
