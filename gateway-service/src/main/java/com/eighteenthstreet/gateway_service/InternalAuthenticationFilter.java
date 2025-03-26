package com.eighteenthstreet.gateway_service;

import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class InternalAuthenticationFilter implements GlobalFilter, Ordered {
	@Value("${JWT_SECRET_KEY}")
	private String secretKey;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String path = exchange.getRequest().getURI().getPath();
		log.info("path = {}", path);
		// "/internal/" 경로일 경우에만 내부 JWT 삽입
		if (path.startsWith("/internal/")) {
			String token = createInternalToken();

			ServerHttpRequest newRequest = exchange.getRequest().mutate()
				.header("Authorization", "Bearer " + token)
				.build();

			return chain.filter(exchange.mutate().request(newRequest).build());
		}

		return chain.filter(exchange);
	}

	@Override
	public int getOrder() {
		return -10;
	}

	private String createInternalToken() {
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

		return Jwts.builder()
			.subject("internal-service")
			.issuer("gateway")
			.audience().add("user-service").and()
			.claim("role", "internal")
			.expiration(Date.from(Instant.now().plusSeconds(600))) // 10분 유효
			.signWith(key)
			.compact();
	}
}
