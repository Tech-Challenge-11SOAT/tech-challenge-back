package br.com.postech.techchallange.domain.model;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Produto {
	private Long id;
	private String nome;
	private String descricao;
	private BigDecimal preco;
	private Long idImagemUrl;
	private Long idCategoria;

	public Produto(Long id, String nome, String descricao, BigDecimal preco, Long idImagemUrl, Long idCategoria) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.preco = preco;
		this.idImagemUrl = idImagemUrl;
		this.idCategoria = idCategoria;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public Long getIdImagemUrl() {
		return idImagemUrl;
	}

	public void setIdImagemUrl(Long idImagemUrl) {
		this.idImagemUrl = idImagemUrl;
	}

	public Long getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(Long idCategoria) {
		this.idCategoria = idCategoria;
	}

}
