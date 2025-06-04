package br.com.postech.techchallange.domain.port.out;

import java.util.List;
import java.util.Optional;

import br.com.postech.techchallange.domain.model.Pagamento;

public interface PagamentoRepositoryPort {

    Pagamento salvar(Pagamento pagamento);

    Optional<Pagamento> buscarPorId(Long id);

    Optional<Pagamento> buscarPorIdPedido(Long idPedido);

    List<Pagamento> listarTodos();
}
