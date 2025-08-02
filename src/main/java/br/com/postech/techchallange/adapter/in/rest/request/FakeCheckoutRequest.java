package br.com.postech.techchallange.adapter.in.rest.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;

public class FakeCheckoutRequest {

	private Long idCliente;
	@NotNull
	private List<ItemProduto> produtos;

	private String metodoPagamento;

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

	public String getMetodoPagamento() {
		return metodoPagamento;
	}

	public void setMetodoPagamento(String metodoPagamento) {
		this.metodoPagamento = metodoPagamento;
	}

	@EqualsAndHashCode
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
