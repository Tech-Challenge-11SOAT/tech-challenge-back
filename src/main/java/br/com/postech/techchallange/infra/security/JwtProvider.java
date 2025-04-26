package br.com.postech.techchallange.infra.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {

	private static final MacAlgorithm SIGNATURE_ALGORITHM = Jwts.SIG.HS256;
	private SecretKey key;
	private final JwtProperties jwtProperties;

	public JwtProvider(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;
	}

	@PostConstruct
	public void init() {
		this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
	}

	public String generateToken(Long idAdmin, String email) {
		return Jwts.builder()
				.subject(email)
				.claim("idAdmin", idAdmin)
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()))
				.signWith(key, SIGNATURE_ALGORITHM)
				.compact();
	}

	public String getEmailFromToken(String token) {
		return parseClaims(token).getPayload().getSubject();
	}

	public boolean validateToken(String token) {
		try {
			this.parseClaims(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	private Jws<Claims> parseClaims(String token) {
		return Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
	}
}
