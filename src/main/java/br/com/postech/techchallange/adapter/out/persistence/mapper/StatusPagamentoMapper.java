package br.com.postech.techchallange.adapter.out.persistence.mapper;

import br.com.postech.techchallange.adapter.out.persistence.entity.StatusPagamentoEntity;
import br.com.postech.techchallange.domain.enums.StatusPagamentoEnum;
import br.com.postech.techchallange.domain.model.StatusPagamento;

public class StatusPagamentoMapper {

	public static StatusPagamento toDomain(StatusPagamentoEntity entity) {
		return StatusPagamento.builder()
				.idStatusPagamento(entity.getIdStatusPagamento())
				.nomeStatus(StatusPagamentoEnum.of(entity.getNomeStatus()))
				.build();
	}

	public static StatusPagamentoEntity toEntity(StatusPagamento domain) {
		return StatusPagamentoEntity.builder()
				.idStatusPagamento(domain.getIdStatusPagamento())
				.nomeStatus(domain.getNomeStatus().getStatus())
				.build();
	}
}
