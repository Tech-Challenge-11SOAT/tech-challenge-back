package br.com.postech.techchallange.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.postech.techchallange.domain.model.StatusPagamento;
import br.com.postech.techchallange.domain.port.in.GerenciarStatusPagamentoUseCase;
import br.com.postech.techchallange.domain.port.out.StatusPagamentoRepositoryPort;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GerenciarStatusPagamentoService implements GerenciarStatusPagamentoUseCase {

	private final StatusPagamentoRepositoryPort repository;

	@Override
	public StatusPagamento buscarStatusPagamento(Long id) {
		return repository.buscarPorId(id).orElse(null);
	}

	@Override
	public List<StatusPagamento> listarStatusPagamentos() {
		return repository.listarTodos();
	}
}
