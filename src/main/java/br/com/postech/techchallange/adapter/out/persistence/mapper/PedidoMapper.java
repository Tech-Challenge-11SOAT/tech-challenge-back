package br.com.postech.techchallange.adapter.out.persistence.mapper;

import br.com.postech.techchallange.domain.model.Pedido;
import br.com.postech.techchallange.adapter.out.persistence.entity.PedidoEntity;

public class PedidoMapper {

	public static Pedido toDomain(PedidoEntity entity) {
		return new Pedido(
				entity.getIdPedido(), 
				entity.getIdCliente(),
				entity.getDataPedido(),
				entity.getIdStatusPedido(),
				entity.getDataStatus()
				);
	}

	public static PedidoEntity toEntity(Pedido pedido) {
		PedidoEntity entity = new PedidoEntity();
		entity.setIdPedido(pedido.getId());
		entity.setIdCliente(pedido.getIdCliente());
		entity.setDataPedido(pedido.getDataPedido());
		entity.setIdStatusPedido(pedido.getIdStatusPedido());
		entity.setDataStatus(pedido.getDataStatus());
		return entity;
	}
}
