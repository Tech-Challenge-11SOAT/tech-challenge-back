package br.com.postech.techchallange.adapter.out.redis;

import br.com.postech.techchallange.domain.port.out.RedisPort;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.Optional;

public abstract class RedisAbstractService<K, V> implements RedisPort<K, V> {

	private final RedisTemplate<K, V> redisTemplate;
	private final Duration ttl;

	protected RedisAbstractService(RedisTemplate<K, V> redisTemplate, Duration ttl) {
		this.redisTemplate = redisTemplate;
		this.ttl = ttl;
	}

	@Override
	public void salvar(K chave, V valor) {
		redisTemplate.opsForValue().set(chave, valor, ttl);
	}

	@Override
	public Optional<V> obter(K chave) {
		return Optional.ofNullable(redisTemplate.opsForValue().get(chave));
	}

	@Override
	public void remover(K chave) {
		redisTemplate.delete(chave);
	}
}