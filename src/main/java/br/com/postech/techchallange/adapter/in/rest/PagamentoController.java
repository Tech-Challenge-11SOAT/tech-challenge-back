package br.com.postech.techchallange.adapter.in.rest;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.postech.techchallange.domain.model.Pagamento;
import br.com.postech.techchallange.domain.port.in.GerenciarPagamentoUseCase;
import br.com.postech.techchallange.domain.port.in.ProcessarPagamentoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/open/pagamentos")
@RequiredArgsConstructor
@Tag(name = "Pagamentos", description = "Operações de pagamento")
public class PagamentoController {

	private final GerenciarPagamentoUseCase pagamentoUseCase;
	private final ProcessarPagamentoUseCase processarPagamento;
	
	@PostMapping
	@Operation(summary = "Criar pagamento")
	public Pagamento criar(@RequestBody @Valid Pagamento pagamento) {
		return this.pagamentoUseCase.pagar(pagamento);
	}
	
	@Operation(summary = "Obter QR Code já gerado (Redis) de um pagamento pelo ID do pedido")
	@GetMapping("/{id}/qrcode")
	public String obterQrCode(@PathVariable Long id) {
		return this.processarPagamento.obterQRCode(id.toString());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar pagamento por ID")
	public Pagamento buscar(@PathVariable Long id) {
		return this.pagamentoUseCase.buscarPagamento(id);
	}

	@GetMapping
	@Operation(summary = "Listar pagamentos")
	public List<Pagamento> listar() {
		return this.pagamentoUseCase.listarPagamentos();
	}
}
