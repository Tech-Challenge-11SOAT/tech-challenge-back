package br.com.postech.techchallange.infra.cache;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisService {

	private final RedisTemplate<String, String> redisTemplate;

	public void salvarQRCode(Long idPedido, String qrCodeBase64) {
		redisTemplate.opsForValue().set(
				"qrcode:pedido:" + idPedido,
				qrCodeBase64,
				5,
				TimeUnit.MINUTES
			);
	}

	public String obterQRCode(Long idPedido) {
		return redisTemplate.opsForValue().get("qrcode:pedido:" + idPedido);
	}

	public void removerQRCode(Long idPedido) {
		redisTemplate.delete("qrcode:pedido:" + idPedido);
	}
}
