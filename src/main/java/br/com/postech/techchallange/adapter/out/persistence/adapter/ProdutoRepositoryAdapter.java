package br.com.postech.techchallange.adapter.out.persistence.adapter;

import br.com.postech.techchallange.domain.model.Produto;
import br.com.postech.techchallange.domain.port.out.ProdutoRepositoryPort;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

@Component
public class ProdutoRepositoryAdapter implements ProdutoRepositoryPort {
	@Override
	public Produto buscarPorId(Long id) {
		return new Produto(id, "aa", new BigDecimal("0"));
	}
}
