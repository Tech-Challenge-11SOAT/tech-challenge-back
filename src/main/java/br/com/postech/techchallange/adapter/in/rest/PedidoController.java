package br.com.postech.techchallange.adapter.in.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.postech.techchallange.adapter.in.rest.request.FakeCheckoutRequest;
import br.com.postech.techchallange.adapter.in.rest.response.PedidoCompletoResponse;
import br.com.postech.techchallange.adapter.in.rest.response.PedidoPagamentoResponse;
import br.com.postech.techchallange.domain.model.Pedido;
import br.com.postech.techchallange.domain.port.in.FakeCheckoutUseCase;
import br.com.postech.techchallange.domain.port.in.GerenciarPedidoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/open/pedidos")
@Tag(name = "Pedidos", description = "Operações relacionadas aos pedidos")
public class PedidoController {

    private final GerenciarPedidoUseCase pedidoUseCase;
    @Autowired
    private FakeCheckoutUseCase fakeCheckoutService;

    public PedidoController(GerenciarPedidoUseCase pedidoUseCase) {
        this.pedidoUseCase = pedidoUseCase;
    }

    @Operation(summary = "Criar um novo pedido")
    @PostMapping
    public Pedido criar(
            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do pedido a ser criado",
                    required = true) Pedido pedido) {
        return pedidoUseCase.criarPedido(pedido);
    }

    @Operation(summary = "Buscar um pedido pelo ID")
    @GetMapping("/{id}")
    public Pedido buscar(@Parameter(description = "ID do pedido", example = "1") @PathVariable Long id) {
        return pedidoUseCase.buscarPedido(id);
    }

    @Operation(summary = "Listar todos os pedidos")
    @GetMapping
    public List<PedidoCompletoResponse> listar() {
        return this.pedidoUseCase.listarPedidos();
    }

    @Operation(summary = "Listar os pedidos por status")
    @GetMapping("por-status")
    public List<PedidoCompletoResponse> listarPedidosPorStatus(@RequestParam Long statusId) {
        return this.pedidoUseCase.listarPorStatus(statusId);
    }

	@Operation(summary = "Fake checkout: envia produtos para a fila (simulação de finalização do pedido)")
	@PostMapping("/checkout")
	public ResponseEntity<PedidoPagamentoResponse> fakeCheckout(@RequestBody @Valid FakeCheckoutRequest request) {
		PedidoPagamentoResponse response = fakeCheckoutService.processarFakeCheckout(request);
		return ResponseEntity.ok(response);
	}
}
