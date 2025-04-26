package br.com.postech.techchallange.infra.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
	private String secret;
	private long refreshTokenExpiration;
	private long accessTokenExpiration;
}
