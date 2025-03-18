package auth;

import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtil {
	@Value("${service.jwt.secret-key}")
	private String secretKey;

	public UUID getUserIdFromToken(String authorization) {
		String token = getToken(authorization);

		if (token == null) {
			return null;
		}

		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
		Claims claims = Jwts.parser()
			.verifyWith(key)
			.build()
			.parseSignedClaims(token)
			.getBody();

		return UUID.fromString(claims.get("userId", String.class));
	}

	public Role getRoleFromToken(String authorization) {
		String token = getToken(authorization);

		if (token == null) {
			return null;
		}

		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
		Claims claims = Jwts.parser()
			.verifyWith(key)
			.build()
			.parseSignedClaims(token)
			.getBody();

		return Role.valueOf(claims.get("role", String.class));
	}

	public String getUsernameFromToken(String authorization) {
		String token = getToken(authorization);

		if (token == null) {
			return null;
		}

		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
		Claims claims = Jwts.parser()
			.verifyWith(key)
			.build()
			.parseSignedClaims(token)
			.getBody();

		return claims.get("username", String.class);
	}

	private String getToken(String authorization) {
		if (authorization == null || !authorization.startsWith("Bearer ")) {
			log.info("Invalid authorization token");
			return null;
		}
		return authorization.replace("Bearer ", "");
	}
}
