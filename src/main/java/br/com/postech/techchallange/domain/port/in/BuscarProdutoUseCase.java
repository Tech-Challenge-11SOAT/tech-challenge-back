package br.com.postech.techchallange.domain.port.in;

import java.util.Optional;

import br.com.postech.techchallange.domain.model.Produto;

public interface BuscarProdutoUseCase {
	Optional<Produto> buscarPorId(Long id);
}
