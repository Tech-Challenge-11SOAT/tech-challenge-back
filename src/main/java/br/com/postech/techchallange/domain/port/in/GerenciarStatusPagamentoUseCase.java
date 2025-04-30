package br.com.postech.techchallange.domain.port.in;

import java.util.List;

import br.com.postech.techchallange.domain.model.StatusPagamento;

public interface GerenciarStatusPagamentoUseCase {
	StatusPagamento buscarStatusPagamento(Long id);

	List<StatusPagamento> listarStatusPagamentos();
}
