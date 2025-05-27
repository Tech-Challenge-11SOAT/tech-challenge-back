package br.com.postech.techchallange.adapter.out.persistence.mapper;

import br.com.postech.techchallange.adapter.out.persistence.entity.ProdutoEntity;
import br.com.postech.techchallange.domain.model.Produto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoMapperTest {

    @Test
    @DisplayName("Deve converter ProdutoEntity para ProdutoDomain")
    void deveConverterProdutoEntityParaProdutoDomain() {
        // Arrange
        ProdutoEntity entity = ProdutoEntity.builder()
                .idProduto(1L)
                .nome("Produto Teste")
                .descricao("Descrição de teste")
                .preco(BigDecimal.valueOf(19.99))
                .idImagemUrl(101L)
                .idCategoria(5L)
                .build();

        // Act
        Produto domain = ProdutoMapper.toDomain(entity);

        // Assert
        assertNotNull(domain);
        assertEquals(entity.getIdProduto(), domain.getId());
        assertEquals(entity.getNome(), domain.getNome());
        assertEquals(entity.getDescricao(), domain.getDescricao());
        assertEquals(entity.getPreco(), domain.getPreco());
        assertEquals(entity.getIdImagemUrl(), domain.getIdImagemUrl());
        assertEquals(entity.getIdCategoria(), domain.getIdCategoria());
    }

    @Test
    @DisplayName("Deve converter ProdutoDomain para ProdutoEntity")
    void deveConverterProdutoDomainParaProdutoEntity() {
        // Arrange
        Produto domain = Produto.builder()
                .id(2L)
                .nome("Produto Domain")
                .descricao("Descrição do domain")
                .preco(BigDecimal.valueOf(49.90))
                .idImagemUrl(202L)
                .idCategoria(10L)
                .build();

        // Act
        ProdutoEntity entity = ProdutoMapper.toEntity(domain);

        // Assert
        assertNotNull(entity);
        assertEquals(domain.getId(), entity.getIdProduto());
        assertEquals(domain.getNome(), entity.getNome());
        assertEquals(domain.getDescricao(), entity.getDescricao());
        assertEquals(domain.getPreco(), entity.getPreco());
        assertEquals(domain.getIdImagemUrl(), entity.getIdImagemUrl());
        assertEquals(domain.getIdCategoria(), entity.getIdCategoria());
    }

}