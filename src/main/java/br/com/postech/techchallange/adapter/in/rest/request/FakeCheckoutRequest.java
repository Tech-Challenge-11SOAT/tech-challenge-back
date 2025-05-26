package br.com.postech.techchallange.adapter.in.rest.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public class FakeCheckoutRequest {

	@NotNull
	private Long idCliente;
	@NotNull
	private List<ItemProduto> produtos;

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public List<ItemProduto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<ItemProduto> produtos) {
		this.produtos = produtos;
	}

	public static class ItemProduto {

		@NotNull
		private Long idProduto;
		@NotNull
		private Integer quantidade;

		public Long getIdProduto() {
			return idProduto;
		}

		public void setIdProduto(Long idProduto) {
			this.idProduto = idProduto;
		}

		public Integer getQuantidade() {
			return quantidade;
		}

		public void setQuantidade(Integer quantidade) {
			this.quantidade = quantidade;
		}
	}
}
