package br.com.postech.techchallange.domain.port.out;

import br.com.postech.techchallange.domain.model.Pagamento;
import java.util.List;
import java.util.Optional;

public interface PagamentoRepositoryPort {
	Pagamento salvar(Pagamento pagamento);

	Optional<Pagamento> buscarPorId(Long id);

	List<Pagamento> listarTodos();
}
