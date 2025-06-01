package br.com.postech.techchallange.adapter.in.rest.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClienteMetricaResponse {

	private Long idCliente;
	private String nome;
	private String email;
	private Integer totalPedidos;
	private BigDecimal totalGasto;
	private LocalDateTime dataUltimoPedido;
	private Long idUltimoPedido;
	private List<PedidoResumo> pedidos;

	@Data
	@Builder
	public static class PedidoResumo {

		private Long idPedido;
		private LocalDateTime dataPedido;
		private BigDecimal valorTotal;
		private String status;
	}
}
