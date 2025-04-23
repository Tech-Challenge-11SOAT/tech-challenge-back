package br.com.postech.techchallange.adapter.in.rest;

import br.com.postech.techchallange.domain.model.Pagamento;
import br.com.postech.techchallange.domain.port.in.GerenciarPagamentoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagamentos")
@Tag(name = "Pagamentos", description = "Operações de pagamento")
public class PagamentoController {

	private final GerenciarPagamentoUseCase pagamentoUseCase;

	public PagamentoController(GerenciarPagamentoUseCase pagamentoUseCase) {
		this.pagamentoUseCase = pagamentoUseCase;
	}

	@PostMapping
	@Operation(summary = "Criar pagamento")
	public Pagamento criar(@RequestBody @Valid Pagamento pagamento) {
		return pagamentoUseCase.criarPagamento(pagamento);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar pagamento por ID")
	public Pagamento buscar(@PathVariable Long id) {
		return pagamentoUseCase.buscarPagamento(id);
	}

	@GetMapping
	@Operation(summary = "Listar pagamentos")
	public List<Pagamento> listar() {
		return pagamentoUseCase.listarPagamentos();
	}
}
