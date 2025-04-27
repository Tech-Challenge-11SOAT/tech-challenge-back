package br.com.postech.techchallange.infra.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import br.com.postech.techchallange.domain.model.AdminRole;
import br.com.postech.techchallange.domain.model.AdminUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.lang.Collections;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import jakarta.annotation.PostConstruct;
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

	public String generateAccessToken(AdminUser admin) {
		List<String> roles = Optional.ofNullable(admin.getRoles())
				.orElse(Collections.emptyList())
				.stream()
				.map(AdminRole::getNome)
				.toList();

		return Jwts.builder()
				.subject(admin.getEmail())
				.claim("idAdmin", admin.getId())
				.claim("roles", roles)
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessTokenExpiration()))
				.signWith(key, SIGNATURE_ALGORITHM)
				.compact();
	}

	public String generateRefreshToken(AdminUser admin) {
		return Jwts.builder()
				.subject(admin.getEmail())
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + jwtProperties.getRefreshTokenExpiration()))
				.signWith(key, SIGNATURE_ALGORITHM)
				.compact();
	}

	public String getCurrentUserEmail() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new RuntimeException("Usuário não autenticado");
		}
		return authentication.getName();
	}

	public String getEmailFromToken(String token) {
		return this.parseClaims(token).getPayload().getSubject();
	}

	public boolean validateToken(String token) {
		try {
			this.parseClaims(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	public Claims getClaims(String token) {
		return this.parseClaims(token).getPayload();
	}

	private Jws<Claims> parseClaims(String token) {
		return Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
	}
	
	public String extractToken(String bearerToken) {
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		throw new RuntimeException("Token inválido");
	}

}
