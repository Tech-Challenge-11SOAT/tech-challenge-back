package br.com.postech.techchallange.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		long startTime = System.currentTimeMillis();

		String method = request.getMethod();
		String uri = request.getRequestURI();
		String ip = request.getRemoteAddr();
		String userAgent = request.getHeader("User-Agent");
		String requestId = request.getHeader("X-Request-ID");

		logger.info("Início requisição - [{}] {} - IP: {} - Agente: {} - Request-ID: {}", method, uri, ip, userAgent, requestId);

		filterChain.doFilter(request, response);

		long duration = System.currentTimeMillis() - startTime;
		int status = response.getStatus();

		logger.info("Fim requisição - [{}] {} - Status: {} - Tempo: {} ms - Request-ID: {}", method, uri, status, duration, requestId);
	}
}
