package util;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	@Value("${service.jwt.secret-key}")
	private String secretKey;

	public String getUserIdFromToken(String token) {
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
		Claims claims = Jwts.parser()
			.verifyWith(key)
			.build()
			.parseSignedClaims(token)
			.getBody();

		return claims.get("userId", String.class);
	}

	public String getRoleFromToken(String token) {
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
		Claims claims = Jwts.parser()
			.verifyWith(key)
			.build()
			.parseSignedClaims(token)
			.getBody();

		return claims.get("role", String.class);
	}

	public String getUsernameFromToken(String token) {
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
		Claims claims = Jwts.parser()
			.verifyWith(key)
			.build()
			.parseSignedClaims(token)
			.getBody();

		return claims.get("username", String.class);
	}
}
