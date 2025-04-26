package br.com.postech.techchallange.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		final String securitySchemeName = "bearerAuth";

		return new OpenAPI()
				.info(new Info()
						.title("TechChallenge")
						.version("1.0.0")
						.description("Documentação da API para gerenciamento de pedidos, clientes e administração de usuários."))
				.components(new Components()
						.addSecuritySchemes(securitySchemeName,
								new SecurityScheme()
								.type(SecurityScheme.Type.HTTP)
								.scheme("bearer")
								.bearerFormat("JWT")
								.description("**Access Token** necessário para acessar os endpoints protegidos.")))
				.addSecurityItem(new SecurityRequirement()
						.addList(securitySchemeName));
	}
}
