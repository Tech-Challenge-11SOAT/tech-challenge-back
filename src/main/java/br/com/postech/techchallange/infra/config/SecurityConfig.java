package br.com.postech.techchallange.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.postech.techchallange.domain.constants.SecurityConstants;
import br.com.postech.techchallange.infra.security.JwtAuthenticationFilter;
import br.com.postech.techchallange.infra.security.JwtProvider;
import br.com.postech.techchallange.infra.security.TokenBlacklistService;

@Configuration
public class SecurityConfig {

	private final JwtProvider jwtProvider;
	private final TokenBlacklistService tokenBlacklistService;

	public SecurityConfig(JwtProvider jwtProvider, TokenBlacklistService tokenBlacklistService) {
		this.jwtProvider = jwtProvider;
		this.tokenBlacklistService = tokenBlacklistService;
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth.requestMatchers(SecurityConstants.AUTHORIZED_URLS).permitAll()
				.requestMatchers("/actuator/**").permitAll()
				.requestMatchers("/admin/roles/**").hasRole("ADMIN").anyRequest().authenticated())
				.addFilterBefore(new RateLimitingFilter(), UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(new JwtAuthenticationFilter(jwtProvider, tokenBlacklistService), UsernamePasswordAuthenticationFilter.class)
				.build();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
}
