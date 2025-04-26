package br.com.postech.techchallange.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;
	private final TokenBlacklistService tokenBlacklistService;

	public JwtAuthenticationFilter(JwtProvider jwtProvider, TokenBlacklistService tokenBlacklistService) {
		this.jwtProvider = jwtProvider;
		this.tokenBlacklistService = tokenBlacklistService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String token = this.parseToken(request);

		if (token != null && jwtProvider.validateToken(token) && !tokenBlacklistService.isTokenBlacklisted(token)) {
			String email = jwtProvider.getEmailFromToken(token);

			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, null,
					Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN")));

			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);
	}

	private String parseToken(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7);
		}

		return null;
	}
}
