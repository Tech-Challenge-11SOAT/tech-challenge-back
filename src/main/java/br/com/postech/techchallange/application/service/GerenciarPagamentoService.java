package br.com.postech.techchallange.application.service;

import br.com.postech.techchallange.domain.model.Pagamento;
import br.com.postech.techchallange.domain.port.in.GerenciarPagamentoUseCase;
import br.com.postech.techchallange.domain.port.out.PagamentoRepositoryPort;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GerenciarPagamentoService implements GerenciarPagamentoUseCase {

	private final PagamentoRepositoryPort repository;

	@Override
	public Pagamento criarPagamento(Pagamento pagamento) {
		return repository.salvar(pagamento);
	}

	@Override
	public Pagamento buscarPagamento(Long id) {
		return repository.buscarPorId(id).orElse(null);
	}

	@Override
	public List<Pagamento> listarPagamentos() {
		return repository.listarTodos();
	}
}
