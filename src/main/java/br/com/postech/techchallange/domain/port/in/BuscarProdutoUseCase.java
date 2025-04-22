package br.com.postech.techchallange.domain.port.in;

import br.com.postech.techchallange.domain.model.Produto;

public interface BuscarProdutoUseCase {
    Produto buscarPorId(Long id);
}
