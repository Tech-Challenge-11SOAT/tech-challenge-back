package br.com.postech.techchallange.adapter.out.persistence.mapper;

import br.com.postech.techchallange.adapter.out.persistence.entity.StatusPagamentoEntity;
import br.com.postech.techchallange.domain.enums.StatusPagamentoEnum;
import br.com.postech.techchallange.domain.model.StatusPagamento;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusPagamentoMapperTest {

    @Test
    @DisplayName("Deve converter StatusPagamentoEntity para StatusPagamentoDomain")
    void deveConverterEntityParaDomain() {

        StatusPagamentoEntity entity = StatusPagamentoEntity.builder()
                .idStatusPagamento(1L)
                .nomeStatus("FINALIZADO")
                .build();

        StatusPagamento domain = StatusPagamentoMapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(1L, domain.getIdStatusPagamento());
        assertEquals(StatusPagamentoEnum.FINALIZADO, domain.getNomeStatus());
    }

    @Test
    @DisplayName("Deve converter StatusPagamentoDomain para StatusPagamentoEntity")
    void deveConverterDomainParaEntity() {

        StatusPagamento domain = StatusPagamento.builder()
                .idStatusPagamento(2L)
                .nomeStatus(StatusPagamentoEnum.PENDENTE)
                .build();

        StatusPagamentoEntity entity = StatusPagamentoMapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(2L, entity.getIdStatusPagamento());
        assertEquals("PENDENTE", entity.getNomeStatus());
    }
}
