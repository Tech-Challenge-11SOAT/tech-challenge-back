package br.com.postech.techchallange.application.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.postech.techchallange.domain.enums.StatusPagamentoEnum;
import br.com.postech.techchallange.domain.model.Pagamento;
import br.com.postech.techchallange.domain.model.StatusPagamento;
import br.com.postech.techchallange.domain.port.in.GerenciarPagamentoUseCase;
import br.com.postech.techchallange.domain.port.in.GerenciarStatusPagamentoUseCase;
import br.com.postech.techchallange.domain.port.out.MercadoPagoPort;
import br.com.postech.techchallange.domain.port.out.PagamentoRepositoryPort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GerenciarPagamentoService implements GerenciarPagamentoUseCase {

	private final PagamentoRepositoryPort repository;
	private final MercadoPagoPort mercadoPagoPort;
	private final GerenciarStatusPagamentoUseCase gerenciarStatusPagamentoUseCase;

	@Override
	public Pagamento pagar(Pagamento pagamento) {
		pagamento.setDataPagamento(LocalDateTime.now());
		long idStatusPendente = this.gerenciarStatusPagamentoUseCase.listarStatusPagamentos().stream()
				.filter(status -> StatusPagamentoEnum.PENDENTE.name().equals(status.getNomeStatus().getStatus()))
				.mapToLong(StatusPagamento::getIdStatusPagamento).findFirst()
				.orElseThrow(() -> new IllegalStateException("Status PENDENTE não encontrado"));
		
		pagamento.setIdStatusPagamento(idStatusPendente);

		Pagamento salvo = repository.salvar(pagamento);

		String qrCode = mercadoPagoPort.gerarQRCode(salvo);

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
