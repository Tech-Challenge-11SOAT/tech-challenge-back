package br.com.postech.techchallange.adapter.in.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.postech.techchallange.domain.model.StatusPagamento;
import br.com.postech.techchallange.domain.port.in.GerenciarStatusPagamentoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/open/status-pagamento")
@Tag(name = "Status Pagamento", description = "Operações de status de pagamento")
public class StatusPagamentoController {

	private final GerenciarStatusPagamentoUseCase statusPagamentoUseCase;

	public StatusPagamentoController(GerenciarStatusPagamentoUseCase statusPagamentoUseCase) {
		this.statusPagamentoUseCase = statusPagamentoUseCase;
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar status de pagamento por ID")
	public StatusPagamento buscar(@PathVariable Long id) {
		return statusPagamentoUseCase.buscarStatusPagamento(id);
	}

	@GetMapping
	@Operation(summary = "Listar status de pagamento")
	public List<StatusPagamento> listar() {
		return statusPagamentoUseCase.listarStatusPagamentos();
	}
}
