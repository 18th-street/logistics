package com.eighteenthstreet.user_service.infrastructure.security;

import java.io.IOException;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InternalJwtAuthFilter extends OncePerRequestFilter {

	@Value("${JWT_SECRET_KEY}")
	private String secretKey;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String auth = request.getHeader("Authorization");
		if (auth != null && auth.startsWith("Bearer ")) {
			try {
				SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
				Claims claims = Jwts.parser()
					.verifyWith(key)
					.build()
					.parseSignedClaims(auth.substring(7))
					.getBody();

				// 내부 서비스 인증인지 확인
				if ("gateway".equals(claims.getIssuer()) &&
					"internal".equals(claims.get("role", String.class))) {

					UsernamePasswordAuthenticationToken authentication =
						new UsernamePasswordAuthenticationToken("internal-service", null, List.of());
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}

			} catch (Exception e) {
				log.warn("Internal JWT 검증 실패: {}", e.getMessage());
			}
		}

		filterChain.doFilter(request, response);
	}
}