package com.eighteenthstreet.slack_service.infrastructure.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {
	@Override
	public Optional<String> getCurrentAuditor() {
		ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		if (attributes == null) {
			return Optional.empty();
		}

		HttpServletRequest request = attributes.getRequest();
		String username = request.getHeader("X-User-Username");

		return Optional.ofNullable(username);
	}
}
