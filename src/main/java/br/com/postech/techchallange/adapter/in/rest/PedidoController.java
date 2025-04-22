package br.com.postech.techchallange.adapter.in.rest;

import br.com.postech.techchallange.domain.model.Pedido;
import br.com.postech.techchallange.domain.port.in.GerenciarPedidoUseCase;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

	private final GerenciarPedidoUseCase pedidoUseCase;

	public PedidoController(GerenciarPedidoUseCase pedidoUseCase) {
		this.pedidoUseCase = pedidoUseCase;
	}

	@PostMapping
	public Pedido criar(@RequestBody Pedido pedido) {
		return pedidoUseCase.criarPedido(pedido);
	}

	@GetMapping("/{id}")
	public Pedido buscar(@PathVariable Long id) {
		return pedidoUseCase.buscarPedido(id);
	}

	@GetMapping
	public List<Pedido> listar() {
		return pedidoUseCase.listarPedidos();
	}
}
