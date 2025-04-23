package br.com.postech.techchallange.adapter.out.persistence.mapper;

import br.com.postech.techchallange.adapter.out.persistence.entity.PagamentoEntity;
import br.com.postech.techchallange.domain.model.Pagamento;

public class PagamentoMapper {

	public static Pagamento toDomain(PagamentoEntity entity) {
		return Pagamento.builder().
				id(entity.getIdPagamento())
				.idPedido(entity.getIdPedido())
				.valorTotal(entity.getValorTotal())
				.metodoPagamento(entity.getMetodoPagamento())
				.idStatusPagamento(entity.getIdStatusPagamento())
				.dataPagamento(entity.getDataPagamento())
				.build();
	}

	public static PagamentoEntity toEntity(Pagamento domain) {
		return PagamentoEntity.builder()
				.idPagamento(domain.getId())
				.idPedido(domain.getIdPedido())
				.valorTotal(domain.getValorTotal())
				.metodoPagamento(domain.getMetodoPagamento())
				.idStatusPagamento(domain.getIdStatusPagamento())
				.dataPagamento(domain.getDataPagamento())
				.build();
	}
}
