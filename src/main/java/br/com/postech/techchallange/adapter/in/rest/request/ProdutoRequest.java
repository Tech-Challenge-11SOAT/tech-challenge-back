package br.com.postech.techchallange.adapter.in.rest.request;

import java.math.BigDecimal;

import br.com.postech.techchallange.domain.model.Produto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProdutoRequest(
	@Schema(description = "Nome do produto", example = "Hambúrguer Artesanal")
	@NotBlank String nome,

	@Schema(description = "Descrição do produto", example = "Hambúrguer 200g com queijo e bacon")
	@NotBlank String descricao,

	@Schema(description = "Preço do produto", example = "25.90")
	@NotNull @Positive BigDecimal preco,

	@Schema(description = "ID da imagem do produto", example = "1")
	@NotNull Long idImagemUrl,

	@Schema(description = "ID da categoria do produto", example = "1")
	@NotNull Long idCategoria
) {
	public Produto toDomain() {
		return new Produto(null, nome, descricao, preco, idImagemUrl, idCategoria);
	}
}