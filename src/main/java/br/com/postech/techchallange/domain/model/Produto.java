package br.com.postech.techchallange.domain.model;

import java.math.BigDecimal;

public class Produto {
	private Long id;
	private String nome;
	private BigDecimal preco;

	public Produto(Long id, String nome, BigDecimal preco) {
		this.id = id;
		this.nome = nome;
		this.preco = preco;
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}
}
