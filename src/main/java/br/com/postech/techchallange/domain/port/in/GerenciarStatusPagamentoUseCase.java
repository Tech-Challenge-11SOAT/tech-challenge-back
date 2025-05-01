package br.com.postech.techchallange.domain.port.in;

import java.util.List;

import br.com.postech.techchallange.domain.model.StatusPagamento;

public interface GerenciarStatusPagamentoUseCase {
	StatusPagamento buscarStatusPagamento(Long id);
	
	StatusPagamento buscarStatusPagamentoPorStatus(String status);

	List<StatusPagamento> listarStatusPagamentos();
}
