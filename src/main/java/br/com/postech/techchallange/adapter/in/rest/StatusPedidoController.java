package br.com.postech.techchallange.adapter.in.rest;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.postech.techchallange.domain.model.StatusPedido;
import br.com.postech.techchallange.domain.port.in.GerenciarStatusPedidoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/open/status-pedidos")
@Tag(name = "Status de Pedido", description = "Operações relacionadas ao status do pedido")
public class StatusPedidoController {

	private final GerenciarStatusPedidoUseCase statusPedidoUseCase;

	public StatusPedidoController(GerenciarStatusPedidoUseCase statusPedidoUseCase) {
		this.statusPedidoUseCase = statusPedidoUseCase;
	}

	@GetMapping
	@Operation(summary = "Listar todos os status de pedido")
	public List<StatusPedido> listarTodos() {
		return statusPedidoUseCase.listarTodos();
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "Buscar status de pedido pelo Id")
	public StatusPedido buscarPorId(@PathVariable Long id) {
		return statusPedidoUseCase.buscarStatusPedidoPorId(id);
	}

	@GetMapping("/{nomeStatus}")
	@Operation(summary = "Buscar status de pedido pelo nome")
	public StatusPedido buscarPorNome(@PathVariable String nomeStatus) {
		return statusPedidoUseCase.buscarStatusPedidoPorNome(nomeStatus);
	}
}