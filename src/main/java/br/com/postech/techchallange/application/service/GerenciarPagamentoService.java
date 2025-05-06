package br.com.postech.techchallange.application.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.postech.techchallange.domain.enums.StatusPagamentoEnum;
import br.com.postech.techchallange.domain.model.Pagamento;
import br.com.postech.techchallange.domain.model.Pedido;
import br.com.postech.techchallange.domain.port.in.GerenciarPagamentoUseCase;
import br.com.postech.techchallange.domain.port.in.GerenciarPedidoUseCase;
import br.com.postech.techchallange.domain.port.in.GerenciarStatusPagamentoUseCase;
import br.com.postech.techchallange.domain.port.in.PagamentoValidatorPort;
import br.com.postech.techchallange.domain.port.out.MercadoPagoPort;
import br.com.postech.techchallange.domain.port.out.PagamentoRepositoryPort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GerenciarPagamentoService implements GerenciarPagamentoUseCase {

	private final PagamentoRepositoryPort repository;
	private final MercadoPagoPort mercadoPagoPort;
	private final GerenciarStatusPagamentoUseCase gerenciarStatusPagamento;
	private final GerenciarPedidoUseCase gerenciarPedidoUseCase;
	private final PagamentoValidatorPort pagamentoValidator;

	@Override
	public Pagamento pagar(Pagamento pagamento) {
		Pedido pedido = this.gerenciarPedidoUseCase.buscarPedido(pagamento.getIdPedido());
		this.pagamentoValidator.validar(pedido, pagamento);
		
		pagamento.setDataPagamento(LocalDateTime.now());
		long idStatusPendente = this.gerenciarStatusPagamento
				.buscarStatusPagamentoPorStatus(StatusPagamentoEnum.PENDENTE.getStatus())
				.getIdStatusPagamento();

		pagamento.setIdStatusPagamento(idStatusPendente);

		Pagamento salvo = this.repository.salvar(pagamento);

		Pagamento preferenciaCriada = mercadoPagoPort.criarPreferenciaPagamento(salvo);
		salvo.setInitPoint(preferenciaCriada.getInitPoint());
		repository.salvar(salvo);

		return salvo;
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
