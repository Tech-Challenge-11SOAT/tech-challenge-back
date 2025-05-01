package br.com.postech.techchallange.domain.port.out;

import java.util.Optional;

public interface RedisPort<K, V> {
	void salvar(K chave, V valor);

	Optional<V> obter(K chave);

	void remover(K chave);
}