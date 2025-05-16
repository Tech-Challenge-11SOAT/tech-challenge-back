package br.com.postech.techchallange.adapter.out.persistence.mapper;

import br.com.postech.techchallange.adapter.out.persistence.entity.PedidoEntity;
import br.com.postech.techchallange.domain.model.Pedido;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PedidoMapperTest {

    @Test
    @DisplayName("Deve converter PedidoEntity para PedidoDomain")
    void deveConverterPedidoEntityParaPedidoDomain() {
        // Arrange
        PedidoEntity entity = new PedidoEntity();
        entity.setIdPedido(1L);
        entity.setIdCliente(100L);
        entity.setDataPedido(LocalDateTime.of(2024, 5, 10, 10, 0));
        entity.setIdStatusPedido(1L);
        entity.setDataStatus(LocalDateTime.of(2024, 5, 10, 11, 0));

        // Act
        Pedido pedido = PedidoMapper.toDomain(entity);

        // Assert
        assertNotNull(pedido);
        assertEquals(1L, pedido.getId());
        assertEquals(100L, pedido.getIdCliente());
        assertEquals(LocalDateTime.of(2024, 5, 10, 10, 0), pedido.getDataPedido());
        assertEquals(1L, pedido.getIdStatusPedido());
        assertEquals(LocalDateTime.of(2024, 5, 10, 11, 0), pedido.getDataStatus());
    }

    @Test
    @DisplayName("Deve converter PedidoDomain para PedidoEntity")
    void deveConverterPedidoDomainParaPedidoEntity() {
        // Arrange
        Pedido pedido = new Pedido(
                2L,
                200L,
                LocalDateTime.of(2024, 5, 15, 14, 30),
                2L,
                LocalDateTime.of(2024, 5, 15, 15, 0)
        );

        // Act
        PedidoEntity entity = PedidoMapper.toEntity(pedido);

        // Assert
        assertNotNull(entity);
        assertEquals(2L, entity.getIdPedido());
        assertEquals(200L, entity.getIdCliente());
        assertEquals(LocalDateTime.of(2024, 5, 15, 14, 30), entity.getDataPedido());
        assertEquals(2L, entity.getIdStatusPedido());
        assertEquals(LocalDateTime.of(2024, 5, 15, 15, 0), entity.getDataStatus());
    }
}
