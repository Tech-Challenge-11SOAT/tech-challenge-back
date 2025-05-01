package br.com.postech.techchallange.domain.model;

import java.math.BigDecimal;

public class PedidoProduto {
	private Long id;
	private Long idPedido;
	private Long idProduto;
	private Integer quantidade;
	private BigDecimal precoUnitario;

	public PedidoProduto(Long id, Long idPedido, Long idProduto, Integer quantidade, BigDecimal precoUnitario) {
		this.id = id;
		this.idPedido = idPedido;
		this.idProduto = idProduto;
		this.quantidade = quantidade;
		this.precoUnitario = precoUnitario;
	}

	public Long getId() {
		return id;
	}

	public Long getIdPedido() {
		return idPedido;
	}

	public Long getIdProduto() {
		return idProduto;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public BigDecimal getPrecoUnitario() {
		return precoUnitario;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setIdPedido(Long idPedido) {
		this.idPedido = idPedido;
	}

	public void setIdProduto(Long idProduto) {
		this.idProduto = idProduto;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public void setPrecoUnitario(BigDecimal precoUnitario) {
		this.precoUnitario = precoUnitario;
	}
}