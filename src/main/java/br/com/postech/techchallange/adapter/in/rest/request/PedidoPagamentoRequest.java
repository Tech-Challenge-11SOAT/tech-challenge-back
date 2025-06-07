package br.com.postech.techchallange.adapter.in.rest.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoPagamentoRequest {

    @NotNull
    private Long idCliente;

    @NotNull
    private String metodoPagamento;

    @NotNull
    private List<ItemProdutoRequest> produtos;

    private Integer filaPedido;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemProdutoRequest {

        @NotNull
        private Long idProduto;

        @NotNull
        private Integer quantidade;
    }
}
