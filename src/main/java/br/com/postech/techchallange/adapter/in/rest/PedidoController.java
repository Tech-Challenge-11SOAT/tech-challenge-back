package br.com.postech.techchallange.adapter.in.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.postech.techchallange.adapter.in.rest.request.AtualizarPedidoRequest;
import br.com.postech.techchallange.adapter.in.rest.request.FakeCheckoutRequest;
import br.com.postech.techchallange.adapter.in.rest.response.PedidoCompletoResponse;
import br.com.postech.techchallange.adapter.in.rest.response.PedidoPagamentoResponse;
import br.com.postech.techchallange.domain.enums.StatusPedidoEnum;
import br.com.postech.techchallange.domain.model.Pedido;
import br.com.postech.techchallange.domain.port.in.FakeCheckoutUseCase;
import br.com.postech.techchallange.domain.port.in.GerenciarPedidoUseCase;
import br.com.postech.techchallange.domain.port.in.OrquestradorPedidoPagamentoUseCase;
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

    @Autowired
    private OrquestradorPedidoPagamentoUseCase orquestradorPedidoPagamentoUseCase;

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

    @GetMapping("/cozinha")
    @Operation(summary = "Listar pedidos para cozinha (ordenados e filtrados)")
    public List<PedidoCompletoResponse> listarPedidosCozinha() {
        List<PedidoCompletoResponse> todos = this.pedidoUseCase.listarPedidos();
        List<PedidoCompletoResponse> filtrados = todos.stream()
                .filter(p -> p.status() != null && !"FINALIZADO".equalsIgnoreCase(p.status().nome()))
                .sorted((p1, p2) -> {
                    int statusOrder1 = statusOrder(p1.status().nome());
                    int statusOrder2 = statusOrder(p2.status().nome());
                    if (statusOrder1 != statusOrder2) {
                        return Integer.compare(statusOrder1, statusOrder2);
                    }
                    return p1.dataPedido().compareTo(p2.dataPedido());
                })
                .toList();
        return filtrados;
    }

    private int statusOrder(String status) {
        if ("PRONTO".equalsIgnoreCase(status)) {
            return 0;
        }
        if ("EM_PREPARACAO".equalsIgnoreCase(status) || "EM ANDAMENTO".equalsIgnoreCase(status) || "EM_ANDAMENTO".equalsIgnoreCase(status)) {
            return 1;
        }
        if ("RECEBIDO".equalsIgnoreCase(status) || "RECEBIDO - Não pago".equalsIgnoreCase(status)) {
            return 2;
        }
        return 99;
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status do pedido")
    public PedidoCompletoResponse atualizarStatusPedido(@PathVariable Long id, @RequestParam String novoStatus) {
        AtualizarPedidoRequest req = AtualizarPedidoRequest.builder()
                .id(id)
                .status(StatusPedidoEnum.valueOf(novoStatus))
                .build();
        Pedido pedidoAtualizado = orquestradorPedidoPagamentoUseCase.atualizarPedidoPagamento(req);
        return new PedidoCompletoResponse(
                pedidoAtualizado.getId(),
                pedidoAtualizado.getDataPedido(),
                pedidoAtualizado.getDataStatus(),
                null, // cliente
                new PedidoCompletoResponse.StatusPedido(pedidoAtualizado.getIdStatusPedido(), novoStatus),
                pedidoAtualizado.getFilaPedido() == null ? null : pedidoAtualizado.getFilaPedido().longValue()
        );
    }
}
