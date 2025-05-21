package br.com.postech.techchallange.adapter.out.persistence.mapper;

import br.com.postech.techchallange.adapter.out.persistence.entity.StatusPedidoEntity;
import br.com.postech.techchallange.domain.model.StatusPedido;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusPedidoMapperTest {

    @Test
    @DisplayName("Deve converter StatusPedidoEntity para StatusPedidoDomain")
    void deveConverterEntityParaDomain() {

        StatusPedidoEntity entity = StatusPedidoEntity.builder()
                .idStatusPedido(1L)
                .nomeStatus("Recebido")
                .build();

        StatusPedido domain = StatusPedidoMapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(1L, domain.getIdStatusPedido());
        assertEquals("Recebido", domain.getNomeStatus());
    }

    @Test
    @DisplayName("Deve mapear StatusPedidoDomain para StatusPedidoEntity")
    void deveConverterDomainParaEntity() {

        StatusPedido domain = StatusPedido.builder()
                .idStatusPedido(2L)
                .nomeStatus("Finalizado")
                .build();


        StatusPedidoEntity entity = StatusPedidoMapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(2L, entity.getIdStatusPedido());
        assertEquals("Finalizado", entity.getNomeStatus());
    }
}
