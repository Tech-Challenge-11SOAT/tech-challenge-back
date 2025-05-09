package br.com.postech.techchallange.application.service;

import br.com.postech.techchallange.domain.model.Pedido;
import br.com.postech.techchallange.domain.port.in.GerenciarPedidoUseCase;
import br.com.postech.techchallange.domain.port.out.PedidoRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GerenciarPedidoService implements GerenciarPedidoUseCase {

    private final PedidoRepositoryPort pedidoRepository;

    @Override
    public Pedido criarPedido(Pedido pedido) {
        return this.pedidoRepository.salvar(pedido);
    }

    @Override
    public Pedido buscarPedido(Long id) {
        return this.pedidoRepository.buscarPorId(id).orElse(null);
    }

    @Override
    public List<Pedido> listarPedidos() {
        return this.pedidoRepository.listarTodos();
    }

	@Override
	public Pedido atualizarPedido(Pedido pedido, Long statusId) {
		return this.pedidoRepository.atualizar(pedido, statusId);
	}
}
