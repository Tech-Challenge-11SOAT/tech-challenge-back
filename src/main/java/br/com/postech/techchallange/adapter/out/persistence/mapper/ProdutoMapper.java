package br.com.postech.techchallange.adapter.out.persistence.mapper;

import br.com.postech.techchallange.adapter.out.persistence.entity.ProdutoEntity;
import br.com.postech.techchallange.domain.model.Produto;

public class ProdutoMapper {

	public static Produto toDomain(ProdutoEntity entity) {
		return Produto.builder()
				.id(entity.getIdProduto())
				.nome(entity.getNome())
				.descricao(entity.getDescricao())
				.preco(entity.getPreco())
				.idImagemUrl(entity.getIdImagemUrl())
				.idCategoria(entity.getIdCategoria())
				.build();
	}

	public static ProdutoEntity toEntity(Produto domain) {
		return ProdutoEntity.builder()
				.idProduto(domain.getId())
				.nome(domain.getNome())
				.descricao(domain.getDescricao())
				.preco(domain.getPreco())
				.idImagemUrl(domain.getIdImagemUrl())
				.idCategoria(domain.getIdCategoria())
				.build();
	}
}
