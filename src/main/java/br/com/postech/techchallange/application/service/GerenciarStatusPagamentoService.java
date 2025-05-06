package br.com.postech.techchallange.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.postech.techchallange.domain.model.StatusPagamento;
import br.com.postech.techchallange.domain.port.in.GerenciarStatusPagamentoUseCase;
import br.com.postech.techchallange.domain.port.out.StatusPagamentoRepositoryPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GerenciarStatusPagamentoService implements GerenciarStatusPagamentoUseCase {

	private final StatusPagamentoRepositoryPort repository;

	@Override
	public StatusPagamento buscarStatusPagamento(Long id) {
		return this.repository.buscarPorId(id).orElse(null);
	}

	@Override
	public StatusPagamento buscarStatusPagamentoPorStatus(String status) {
		return this.repository.buscarStatusPagamentoPorStatus(status).orElseThrow(
				() -> new EntityNotFoundException("Status do pagamento não encontrado para o status: " + status));
	}

	@Override
	public List<StatusPagamento> listarStatusPagamentos() {
		return this.repository.listarTodos();
	}
}
