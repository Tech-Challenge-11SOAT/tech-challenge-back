package br.com.postech.techchallange.domain.port.out;

import java.util.List;
import java.util.Optional;

import br.com.postech.techchallange.domain.model.StatusPagamento;

public interface StatusPagamentoRepositoryPort {
	Optional<StatusPagamento> buscarPorId(Long id);

	List<StatusPagamento> listarTodos();
}
