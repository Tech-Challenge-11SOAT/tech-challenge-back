package br.com.postech.techchallange.domain.port.in;

import br.com.postech.techchallange.domain.model.Pagamento;
import java.util.List;

public interface GerenciarPagamentoUseCase {
	Pagamento pagar(Pagamento pagamento);

	Pagamento buscarPagamento(Long id);

	List<Pagamento> listarPagamentos();
}
