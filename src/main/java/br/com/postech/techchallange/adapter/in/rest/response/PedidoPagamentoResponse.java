package br.com.postech.techchallange.adapter.in.rest.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PedidoPagamentoResponse {
	private Long idPedido;
	private Long idPagamento;
	private String metodoPagamento;
	private String status;
	private String initPoint;
}
