package br.com.postech.techchallange.domain.port.out;

public interface EnvironmentConfig {
	String get(String key);

	String get(String key, String defaultValue);
}