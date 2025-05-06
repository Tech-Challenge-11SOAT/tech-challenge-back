package br.com.postech.techchallange.adapter.in.rest.response;

import java.math.BigDecimal;

import br.com.postech.techchallange.domain.model.Produto;
import io.swagger.v3.oas.annotations.media.Schema;

public record ProdutoResponse(
	@Schema(description = "ID do produto", example = "1")
	Long id,

	@Schema(description = "Nome do produto", example = "Hambúrguer Artesanal")
	String nome,

	@Schema(description = "Descrição do produto", example = "Hambúrguer 200g com queijo e bacon")
	String descricao,

	@Schema(description = "Preço do produto", example = "25.90")
	BigDecimal preco,

	@Schema(description = "ID da imagem do produto", example = "1")
	Long idImagemUrl,

	@Schema(description = "ID da categoria do produto", example = "1")
	Long idCategoria
	) {
	public static ProdutoResponse fromDomain(Produto produto) {
		return new ProdutoResponse(
			produto.getId(),
			produto.getNome(),
			produto.getDescricao(),
			produto.getPreco(),
			produto.getIdImagemUrl(),
			produto.getIdCategoria()
		);
	}
}