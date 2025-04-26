package br.com.postech.techchallange.infra.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
		info = @Info(
				title = "TechChallenge API",
				version = "1.0",
				description = "API para gerenciamento administrativo, com autenticação baseada em JWT."
			),
		security = {
			@SecurityRequirement(name = "bearerAuth")
		}
	)
@SecurityScheme(
		name = "bearerAuth",
		type = SecuritySchemeType.HTTP,
		bearerFormat = "JWT",
		scheme = "bearer",
		description = "Insira o token JWT no formato: Bearer {seu_token_aqui}"
)
public class SwaggerConfig {
}
