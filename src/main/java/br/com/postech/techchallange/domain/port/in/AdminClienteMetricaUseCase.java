package br.com.postech.techchallange.domain.port.in;

import br.com.postech.techchallange.adapter.in.rest.response.ClienteMetricaResponse;

import java.util.List;

public interface AdminClienteMetricaUseCase {

	ClienteMetricaResponse obterMetricasCliente(Long idCliente);

	List<ClienteMetricaResponse.PedidoResumo> listarPedidosCliente(Long idCliente);

	ClienteMetricaResponse obterMetricasCliente(Long idCliente, java.time.LocalDateTime dataInicio,
			java.time.LocalDateTime dataFim);

	List<ClienteMetricaResponse.PedidoResumo> listarPedidosCliente(Long idCliente, java.time.LocalDateTime dataInicio,
			java.time.LocalDateTime dataFim);
}
