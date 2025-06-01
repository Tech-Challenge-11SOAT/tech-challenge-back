package br.com.postech.techchallange.adapter.in.rest.response;

import java.time.LocalDateTime;

public record PedidoCompletoResponse(Long id,
		LocalDateTime dataPedido,
		LocalDateTime dataStatus,
		Cliente cliente,
		StatusPedido status,
		Long filaPedido
) {

	public record Cliente(Long id, String nome, String email) {

	}

	public record StatusPedido(Long id, String nome) {

	}
}
