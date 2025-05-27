package br.com.postech.techchallange.application.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.postech.techchallange.adapter.in.rest.request.AtualizarPedidoRequest;
import br.com.postech.techchallange.adapter.in.rest.request.PedidoPagamentoRequest;
import br.com.postech.techchallange.adapter.in.rest.response.PedidoPagamentoResponse;
import br.com.postech.techchallange.domain.enums.StatusPagamentoEnum;
import br.com.postech.techchallange.domain.enums.StatusPedidoEnum;
import br.com.postech.techchallange.domain.model.Pagamento;
import br.com.postech.techchallange.domain.model.Pedido;
import br.com.postech.techchallange.domain.model.PedidoProduto;
import br.com.postech.techchallange.domain.model.Produto;
import br.com.postech.techchallange.domain.model.StatusPedido;
import br.com.postech.techchallange.domain.port.in.GerenciarPedidoUseCase;
import br.com.postech.techchallange.domain.port.in.GerenciarStatusPagamentoUseCase;
import br.com.postech.techchallange.domain.port.in.GerenciarStatusPedidoUseCase;
import br.com.postech.techchallange.domain.port.in.OrquestradorPedidoPagamentoUseCase;
import br.com.postech.techchallange.domain.port.in.PagamentoValidatorPort;
import br.com.postech.techchallange.domain.port.out.PagamentoRepositoryPort;
import br.com.postech.techchallange.domain.port.out.PedidoProdutoRepositoryPort;
import br.com.postech.techchallange.domain.port.out.ProdutoRepositoryPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrquestradorPedidoPagamentoService implements OrquestradorPedidoPagamentoUseCase {

    private final GerenciarPedidoUseCase gerenciarPedido;
    private final GerenciarStatusPedidoUseCase gerenciarStatusPedido;
    private final PedidoProdutoRepositoryPort pedidoProdutoRepository;
    private final ProdutoRepositoryPort produtoRepository;
    private final PagamentoRepositoryPort pagamentoRepository;
    private final GerenciarStatusPagamentoUseCase gerenciarStatusPagamento;
    private final PagamentoValidatorPort pagamentoValidator;

    @Override
    @Transactional
    public PedidoPagamentoResponse orquestrarPedidoPagamento(PedidoPagamentoRequest request) {
        Pedido pedido = this.criarPedido(request);
        List<PedidoProduto> produtos = this.processarProdutosPedido(request, pedido);

        BigDecimal totalCalculado = this.calcularTotalPedido(produtos);
        Pagamento pagamento = this.criarPagamento(request, pedido, totalCalculado);

        this.pagamentoValidator.validar(pedido, pagamento);
        pagamento = this.pagamentoRepository.salvar(pagamento);

        return this.construirPedidoPagamentoResponse(pedido, pagamento);
    }

    private Pedido criarPedido(PedidoPagamentoRequest request) {
        StatusPedido status = gerenciarStatusPedido.buscarStatusPedidoPorNome(StatusPedidoEnum.RECEBIDO_NAO_PAGO.getStatus());
        Pedido pedido = new Pedido(
                null,
                request.getIdCliente(),
                LocalDateTime.now(),
                status.getIdStatusPedido(),
                LocalDateTime.now()
        );
        return gerenciarPedido.criarPedido(pedido);
    }

    private List<PedidoProduto> processarProdutosPedido(PedidoPagamentoRequest request, Pedido pedido) {
        return request.getProdutos().stream().map(prod -> {
            Produto produto = produtoRepository.buscarPorId(prod.getIdProduto())
                    .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));

            PedidoProduto pedidoProduto = new PedidoProduto(
                    null,
                    pedido.getId(),
                    prod.getIdProduto(),
                    prod.getQuantidade(),
                    produto.getPreco()
            );
            this.pedidoProdutoRepository.salvarItemPedido(pedidoProduto);
            return pedidoProduto;
        }).collect(Collectors.toList());
    }

    private BigDecimal calcularTotalPedido(List<PedidoProduto> produtos) {
        return produtos.stream()
                .map(prod -> prod.getPrecoUnitario().multiply(BigDecimal.valueOf(prod.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Pagamento criarPagamento(PedidoPagamentoRequest request, Pedido pedido, BigDecimal totalCalculado) {
        Long idStatusPagamento = this.gerenciarStatusPagamento
                .buscarStatusPagamentoPorStatus(StatusPagamentoEnum.PENDENTE.getStatus())
                .getIdStatusPagamento();

        return Pagamento.builder()
                .idPedido(pedido.getId())
                .metodoPagamento(request.getMetodoPagamento())
                .valorTotal(totalCalculado)
                .dataPagamento(LocalDateTime.now())
                .idStatusPagamento(idStatusPagamento)
                .build();
    }

    private PedidoPagamentoResponse construirPedidoPagamentoResponse(Pedido pedido, Pagamento pagamento) {
        return PedidoPagamentoResponse.builder()
                .idPedido(pedido.getId())
                .idPagamento(pagamento.getId())
                .metodoPagamento(pagamento.getMetodoPagamento())
                .status(StatusPedidoEnum.RECEBIDO_NAO_PAGO.getStatus())
                .build();
    }

    @Override
    public Pedido atualizarPedidoPagamento(AtualizarPedidoRequest request) {
        Pedido pedido = this.gerenciarPedido.buscarPedido(request.getId());
        if (Objects.isNull(pedido)) {
            throw new EntityNotFoundException("Pedido não encontrado");
        }

        Long statusId = this.gerenciarStatusPedido.buscarStatusPedidoPorNome(request.getStatus().getStatus())
                .getIdStatusPedido();

        return this.gerenciarPedido.atualizarPedido(pedido, statusId);
    }
}
