package br.com.postech.techchallange.domain.port.out;

import br.com.postech.techchallange.domain.model.Produto;

public interface ProdutoRepositoryPort {
	Produto buscarPorId(Long id);
}
