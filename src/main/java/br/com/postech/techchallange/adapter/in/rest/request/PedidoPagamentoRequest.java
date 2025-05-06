package br.com.postech.techchallange.adapter.in.rest.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PedidoPagamentoRequest {
	@NotNull
	private Long idCliente;

	@NotNull
	private String metodoPagamento;

	@NotNull
	private List<ItemProdutoRequest> produtos;

	@Data
	public static class ItemProdutoRequest {
		@NotNull
		private Long idProduto;

		@NotNull
		private Integer quantidade;
	}
}