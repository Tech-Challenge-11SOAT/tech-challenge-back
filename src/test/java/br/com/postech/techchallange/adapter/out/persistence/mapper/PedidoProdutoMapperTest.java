package br.com.postech.techchallange.adapter.out.persistence.mapper;

import br.com.postech.techchallange.adapter.out.persistence.entity.PedidoProdutoEntity;
import br.com.postech.techchallange.domain.model.PedidoProduto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PedidoProdutoMapperTest {

    @Test
    @DisplayName("Deve converter PedidoProdutoEntity para PedidoProdutoDomain")
    void deveConverterEntityParaDomain() {
        PedidoProdutoEntity entity = new PedidoProdutoEntity(
                1L,
                10L,
                100L,
                5,
                new BigDecimal("25.50")
        );

        PedidoProduto domain = PedidoProdutoMapper.toDomain(entity);

        assertEquals(entity.getId(), domain.getId());
        assertEquals(entity.getIdPedido(), domain.getIdPedido());
        assertEquals(entity.getIdProduto(), domain.getIdProduto());
        assertEquals(entity.getQuantidade(), domain.getQuantidade());
        assertEquals(entity.getPrecoUnitario(), domain.getPrecoUnitario());
    }

    @Test
    @DisplayName("Deve converter PedidoProdutoDomain para PedidoProdutoEntity")
    void deveConverterDomainParaEntity() {
        PedidoProduto domain = new PedidoProduto(
                2L,
                20L,
                200L,
                3,
                new BigDecimal("40.00")
        );

        PedidoProdutoEntity entity = PedidoProdutoMapper.toEntity(domain);

        assertEquals(domain.getId(), entity.getId());
        assertEquals(domain.getIdPedido(), entity.getIdPedido());
        assertEquals(domain.getIdProduto(), entity.getIdProduto());
        assertEquals(domain.getQuantidade(), entity.getQuantidade());
        assertEquals(domain.getPrecoUnitario(), entity.getPrecoUnitario());
    }
}
