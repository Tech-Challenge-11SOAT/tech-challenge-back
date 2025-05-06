package br.com.postech.techchallange.infra.config;

import br.com.postech.techchallange.domain.port.out.EnvironmentConfig;
import io.github.cdimascio.dotenv.Dotenv;

public class DotEnvConfig implements EnvironmentConfig {
	private final Dotenv dotenv;

	public DotEnvConfig() {
		this.dotenv = Dotenv.configure()
				.ignoreIfMissing()
				.load();
	}

	@Override
	public String get(String key) {
		return dotenv.get(key);
	}

	@Override
	public String get(String key, String defaultValue) {
		String value = dotenv.get(key);
		return value != null ? value : defaultValue;
	}
}
