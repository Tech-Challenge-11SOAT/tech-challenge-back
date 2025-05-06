package br.com.postech.techchallange.adapter.out.persistence.mapper;

import br.com.postech.techchallange.adapter.out.persistence.entity.StatusPedidoEntity;
import br.com.postech.techchallange.domain.model.StatusPedido;

public class StatusPedidoMapper {

	public static StatusPedido toDomain(StatusPedidoEntity entity) {
		if (entity == null) return null;

		return StatusPedido.builder()
				.idStatusPedido(entity.getIdStatusPedido())
				.nomeStatus(entity.getNomeStatus())
				.build();
	}

	public static StatusPedidoEntity toEntity(StatusPedido domain) {
		if (domain == null) return null;

		return StatusPedidoEntity.builder()
				.idStatusPedido(domain.getIdStatusPedido())
				.nomeStatus(domain.getNomeStatus())
				.build();
	}
}