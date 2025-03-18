package com.eighteenthstreet.user_service.infrastructure.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UserAuthenticationFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		String userId = request.getHeader("X-User-Id");
		String role = request.getHeader("X-User-Role");

		if (userId != null && role != null) {
			List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
			Authentication authentication = new UsernamePasswordAuthenticationToken(userId, null, authorities);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);
	}
}