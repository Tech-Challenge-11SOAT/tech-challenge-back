package br.com.postech.techchallange.infra.config.utils;

import br.com.postech.techchallange.domain.port.out.EnvironmentConfig;

public class EnvVars {

	private static EnvironmentConfig config;

	public static void init(EnvironmentConfig customConfig) {
		EnvVars.config = customConfig;
	}

	public static String get(String key) {
		return config.get(key);
	}

	public static String get(String key, String defaultValue) {
		return config.get(key, defaultValue);
	}
}

