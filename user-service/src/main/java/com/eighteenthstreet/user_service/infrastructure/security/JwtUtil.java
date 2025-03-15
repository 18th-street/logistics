package com.eighteenthstreet.user_service.infrastructure.security;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Description;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.eighteenthstreet.user_service.domain.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtUtil {
	private final RedisTemplate<String, String> redisTemplate;

	@Value("${service.jwt.secret-key}")
	private String secretKeyString;

	@Value("${service.jwt.access-expiration}")
	private Long accessExpiration;

	@Value("${service.jwt.refresh-expiration}")
	private Long refreshExpiration;

	private SecretKey secretKey;

	@PostConstruct
	private void init() {
		this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyString));
	}

	public Claims getClaims(String token) {
		return Jwts.parser()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	@Description(
		"엑세스 토큰 생성"
	)
	public String createAccessToken(User user) {
		return Jwts.builder()
			.claim("userId", String.valueOf(user.getUserId()))
			.claim("username", user.getUsername())
			.claim("role", user.getRole().name())
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis() + accessExpiration))
			.signWith(secretKey)
			.compact();
	}

	@Description(
		"리프레시 토큰 생성"
	)
	public String createRefreshToken(User user) {
		String refreshToken = Jwts.builder()
			.claim("userId", String.valueOf(user.getUserId()))
			.claim("username", user.getUsername())
			.claim("role", user.getRole().name())
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis() + refreshExpiration))
			.signWith(secretKey)
			.compact();

		// Redis에 토큰 저장 ( key : "refresh_token:<userId>" )
		redisTemplate.opsForValue()
			.set("refresh_token:" + user.getUserId(), refreshToken, refreshExpiration, TimeUnit.MILLISECONDS);
		return refreshToken;
	}
}
