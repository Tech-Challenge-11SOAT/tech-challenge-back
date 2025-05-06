package br.com.postech.techchallange.infra.config;

public record IntegracaoMercadoPagoConfig(
		String accessToken,
		String userId,
		String externalPosId,
		String notificationUrl) { }
