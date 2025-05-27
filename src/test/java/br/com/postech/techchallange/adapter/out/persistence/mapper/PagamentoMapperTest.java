package br.com.postech.techchallange.adapter.out.persistence.mapper;

import br.com.postech.techchallange.adapter.out.persistence.entity.PagamentoEntity;
import br.com.postech.techchallange.domain.model.Pagamento;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PagamentoMapperTest {

    @Test
    @DisplayName("Deve converter PagamentoEntity para PagamentoDomain")
    void deveConverterEntityParaDomain() {
        PagamentoEntity entity = PagamentoEntity.builder()
                .idPagamento(10L)
                .idPedido(20L)
                .valorTotal(new BigDecimal("150.75"))
                .metodoPagamento("PIX")
                .idStatusPagamento(2L)
                .dataPagamento(LocalDateTime.of(2025, 5, 21, 14, 30))
                .initPoint("https://pagamento.link/initpoint")
                .build();

        Pagamento domain = PagamentoMapper.toDomain(entity);

        assertEquals(entity.getIdPagamento(), domain.getId());
        assertEquals(entity.getIdPedido(), domain.getIdPedido());
        assertEquals(entity.getValorTotal(), domain.getValorTotal());
        assertEquals(entity.getMetodoPagamento(), domain.getMetodoPagamento());
        assertEquals(entity.getIdStatusPagamento(), domain.getIdStatusPagamento());
        assertEquals(entity.getDataPagamento(), domain.getDataPagamento());
        assertEquals(entity.getInitPoint(), domain.getInitPoint());
    }

    @Test
    @DisplayName("Deve converter PagamentoDomain para PagamentoEntity")
    void deveConverterDomainParaEntity() {
        Pagamento domain = Pagamento.builder()
                .id(10L)
                .idPedido(20L)
                .valorTotal(new BigDecimal("150.75"))
                .metodoPagamento("CARTAO")
                .idStatusPagamento(3L)
                .dataPagamento(LocalDateTime.of(2025, 5, 22, 10, 0))
                .initPoint("https://pagamento.link/initpoint2")
                .build();

        PagamentoEntity entity = PagamentoMapper.toEntity(domain);

        assertEquals(domain.getId(), entity.getIdPagamento());
        assertEquals(domain.getIdPedido(), entity.getIdPedido());
        assertEquals(domain.getValorTotal(), entity.getValorTotal());
        assertEquals(domain.getMetodoPagamento(), entity.getMetodoPagamento());
        assertEquals(domain.getIdStatusPagamento(), entity.getIdStatusPagamento());
        assertEquals(domain.getDataPagamento(), entity.getDataPagamento());
        assertEquals(domain.getInitPoint(), entity.getInitPoint());
    }
}
