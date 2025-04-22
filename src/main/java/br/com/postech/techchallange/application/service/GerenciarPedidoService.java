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
        return pedidoRepository.salvar(pedido);
    }

    @Override
    public Pedido buscarPedido(Long id) {
        return pedidoRepository.buscarPorId(id).orElse(null);
    }

    @Override
    public List<Pedido> listarPedidos() {
        return pedidoRepository.listarTodos();
    }
}
