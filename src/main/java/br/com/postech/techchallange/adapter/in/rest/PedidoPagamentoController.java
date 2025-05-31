package br.com.postech.techchallange.adapter.in.rest;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.postech.techchallange.adapter.in.rest.request.AtualizarPedidoRequest;
import br.com.postech.techchallange.adapter.in.rest.request.PedidoPagamentoRequest;
import br.com.postech.techchallange.adapter.in.rest.response.PedidoPagamentoResponse;
import br.com.postech.techchallange.domain.model.Pedido;
import br.com.postech.techchallange.domain.port.in.OrquestradorPedidoPagamentoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/open/pedido-pagamento")
@RequiredArgsConstructor
public class PedidoPagamentoController {

    private final OrquestradorPedidoPagamentoUseCase useCase;

    @PostMapping
    @Operation(summary = "Criar pedido, registrar pagamento")
    public PedidoPagamentoResponse processar(@RequestBody @Valid PedidoPagamentoRequest request) {
        return this.useCase.orquestrarPedidoPagamento(request);
    }

    @PatchMapping
    @Operation(summary = "Atualizar status do pedido de pagamento")
    public Pedido atualizar(@RequestBody @Valid AtualizarPedidoRequest request) {
        return this.useCase.atualizarPedidoPagamento(request);
    }

}
