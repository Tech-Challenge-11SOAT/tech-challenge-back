package br.com.postech.techchallange.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import br.com.postech.techchallange.domain.enums.StatusPagamentoEnum;

class StatusPagamentoTest {

	@Test
	void testBuilder() {
		Long idStatusPagamento = 1L;
		StatusPagamentoEnum nomeStatus = StatusPagamentoEnum.PENDENTE;

		StatusPagamento statusPagamento = StatusPagamento.builder()
				.idStatusPagamento(idStatusPagamento)
				.nomeStatus(nomeStatus)
				.build();

		assertEquals(idStatusPagamento, statusPagamento.getIdStatusPagamento());
		assertEquals(nomeStatus, statusPagamento.getNomeStatus());
	}

	@Test
	void testSettersAndGetters() {
		StatusPagamento statusPagamento = StatusPagamento.builder()
				.idStatusPagamento(1L)
				.nomeStatus(StatusPagamentoEnum.PENDENTE)
				.build();

		Long idStatusPagamento = 1L;
		StatusPagamentoEnum nomeStatus = StatusPagamentoEnum.PENDENTE;

		statusPagamento.setIdStatusPagamento(idStatusPagamento);
		statusPagamento.setNomeStatus(nomeStatus);

		assertEquals(idStatusPagamento, statusPagamento.getIdStatusPagamento());
		assertEquals(nomeStatus, statusPagamento.getNomeStatus());
	}
}