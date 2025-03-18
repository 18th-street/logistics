package com.eighteenthstreet.gateway_service;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import auth.JwtUtil;
import auth.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtAuthenticationFilter implements GlobalFilter {
	private final RedisTemplate<String, String> redisTemplate;
	private final JwtUtil jwtUtil;

	@Value("${service.jwt.secret-key}")
	private String secretKey;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String path = exchange.getRequest().getURI().getPath();

		if (path.equals("/api/v1/users/signUp") || path.equals("/api/v1/users/signIn")) {
			return chain.filter(exchange);
		}

		String token = extractToken(exchange);
		if (token == null || !validation(token)) {
			exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
			return exchange.getResponse().setComplete();
		}

		Long userId = jwtUtil.getUserIdFromToken(token);
		Role role = jwtUtil.getRoleFromToken(token);
		String username = jwtUtil.getUsernameFromToken(token);

		log.info("userId = " + userId + ", role = " + role.name() + ", username = " + username);

		ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
			.header("X-User-Id", String.valueOf(userId))
			.header("X-User-Username", username)
			.header("X-User-Role", role.name())
			.build();

		return chain.filter(exchange.mutate().request(modifiedRequest).build());
	}

	public String extractToken(ServerWebExchange exchange) {
		String authorization = exchange.getRequest().getHeaders().getFirst("Authorization");
		if (authorization != null && authorization.startsWith("Bearer ")) {
			return authorization.substring(7);
		}
		return null;
	}

	private boolean validation(String token) {
		try {
			SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

			Jws<Claims> claimsJws = Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token);

			log.info("### payload :: " + claimsJws.getBody());

			// Redis에서 블랙리스트 조회
			return !redisTemplate.hasKey("blacklist:" + token);
		} catch (Exception e) {
			log.error("JWT 검증 실패 : {}", e.getMessage());
			return false;
		}
	}
}
