package br.com.postech.techchallange.adapter.in.rest;

import br.com.postech.techchallange.domain.model.Pedido;
import br.com.postech.techchallange.domain.port.in.GerenciarPedidoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/open/pedidos")
@Tag(name = "Pedidos", description = "Operações relacionadas aos pedidos")
public class PedidoController {

	private final GerenciarPedidoUseCase pedidoUseCase;

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
					required = true)
			Pedido pedido) {
		return pedidoUseCase.criarPedido(pedido);
	}

	@Operation(summary = "Buscar um pedido pelo ID")
	@GetMapping("/{id}")
	public Pedido buscar(@Parameter(description = "ID do pedido", example = "1") @PathVariable Long id) {
		return pedidoUseCase.buscarPedido(id);
	}

	@Operation(summary = "Listar todos os pedidos")
	@GetMapping
	public List<Pedido> listar() {
		return pedidoUseCase.listarPedidos();
	}
}
