package br.com.postech.techchallange.infra.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<Object> handleExpiredToken(ExpiredJwtException ex, HttpServletRequest request) {
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("status", HttpStatus.UNAUTHORIZED.value());
		body.put("error", "Token Expirado");
		body.put("message", ex.getMessage());
		body.put("path", request.getRequestURI());

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
	}

	@ExceptionHandler(JwtException.class)
	public ResponseEntity<Object> handleInvalidToken(JwtException ex, HttpServletRequest request) {
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("status", HttpStatus.UNAUTHORIZED.value());
		body.put("error", "Token Inválido");
		body.put("message", ex.getMessage());
		body.put("path", request.getRequestURI());

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleGenericException(Exception ex, HttpServletRequest request) {
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
		body.put("error", "Erro Interno");
		body.put("message", ex.getMessage());
		body.put("path", request.getRequestURI());

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
	}
}
