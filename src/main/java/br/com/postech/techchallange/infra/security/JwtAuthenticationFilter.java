package br.com.postech.techchallange.infra.security;

import java.io.IOException;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	private final JwtProvider jwtProvider;
	private final TokenBlacklistService tokenBlacklistService;

	public JwtAuthenticationFilter(JwtProvider jwtProvider, TokenBlacklistService tokenBlacklistService) {
		this.jwtProvider = jwtProvider;
		this.tokenBlacklistService = tokenBlacklistService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String requestURI = request.getRequestURI();
		String token = this.parseToken(request);

		if (token == null) {
			logger.debug("[JWT Filter] Nenhum token presente na requisição: {}", requestURI);
		} else {
			logger.debug("[JWT Filter] Token encontrado para requisição: {}", requestURI);

			if (!jwtProvider.validateToken(token)) {
				logger.warn("[JWT Filter] Token inválido para requisição: {}", requestURI);
			} else if (tokenBlacklistService.isTokenBlacklisted(token)) {
				logger.warn("[JWT Filter] Token na blacklist (logout realizado previamente): {}", requestURI);
			} else {
				String email = jwtProvider.getEmailFromToken(token);
				logger.info("[JWT Filter] Autenticação bem-sucedida para usuário: {} - Rota: {}", email, requestURI);

				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email,
						null, Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN")));

				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		filterChain.doFilter(request, response);
	}

	private String parseToken(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.replace("Bearer ", "");
		}

		return null;
	}
}
