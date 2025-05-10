package br.com.postech.techchallange.infra.security;

import java.util.stream.Stream;

import org.springframework.util.AntPathMatcher;

import br.com.postech.techchallange.domain.constants.SecurityConstants;

public class SecurityUtils {

	private static final AntPathMatcher pathMatcher = new AntPathMatcher();

	public static boolean isPublicEndpoint(String uri) {
		return Stream.of(SecurityConstants.AUTHORIZED_URLS)
				.anyMatch(pattern -> pathMatcher.match(pattern, uri));
	}
}
