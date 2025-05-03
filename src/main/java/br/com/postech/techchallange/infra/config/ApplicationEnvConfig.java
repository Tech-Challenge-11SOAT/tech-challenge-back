package br.com.postech.techchallange.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.postech.techchallange.domain.port.out.EnvironmentConfig;
import br.com.postech.techchallange.infra.config.utils.EnvVars;

@Configuration
public class ApplicationEnvConfig {

	@Bean
	public EnvironmentConfig environmentConfig() {
		return new DotEnvConfig();
	}

	@Bean
	public IntegracaoMercadoPagoConfig integracaoMercadoPagoConfig(EnvironmentConfig config) {
		EnvVars.init(config);

		String token = config.get("MERCADO_PAGO_ACCESS_TOKEN");
		if (token == null || token.isBlank()) {
			throw new IllegalStateException("MERCADO_PAGO_ACCESS_TOKEN não foi definido no ambiente.");
		}

		return new IntegracaoMercadoPagoConfig(token,
				config.get("MERCADO_PAGO_USER_ID"),
				config.get("MERCADO_PAGO_EXTERNAL_POS_ID", ""),
				config.get("MERCADO_PAGO_NOTIFICATION_URL", "http://localhost:8080/listener")
		);
	}
}
