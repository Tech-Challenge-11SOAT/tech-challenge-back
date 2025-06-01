package br.com.postech.techchallange.adapter.in.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

import br.com.postech.techchallange.adapter.in.rest.response.ClienteMetricaResponse;
import br.com.postech.techchallange.domain.port.in.AdminClienteMetricaUseCase;

@RestController
@RequestMapping("/admin/customer/")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin - Usuários", description = "Endpoints para gerenciar os clientes e pedidos do cliente")
public class AdminCustomersController {

	private final AdminClienteMetricaUseCase adminClienteMetricaUseCase;

	@GetMapping("metrics")
	public ResponseEntity<ClienteMetricaResponse> getAnonymousCustomerMetrics(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {
		ClienteMetricaResponse response = adminClienteMetricaUseCase.obterMetricasCliente(null, dataInicio, dataFim);
		return ResponseEntity.ok(response);
	}

	@GetMapping("orders")
	public ResponseEntity<List<ClienteMetricaResponse.PedidoResumo>> listAnonymousCustomerOrders(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {
		List<ClienteMetricaResponse.PedidoResumo> pedidos = adminClienteMetricaUseCase.listarPedidosCliente(null,
				dataInicio, dataFim);
		return ResponseEntity.ok(pedidos);
	}

	@GetMapping("{idCliente}/metrics")
	public ResponseEntity<ClienteMetricaResponse> getCustomerMetrics(@PathVariable Long idCliente,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {
		ClienteMetricaResponse response = adminClienteMetricaUseCase.obterMetricasCliente(idCliente, dataInicio,
				dataFim);
		return ResponseEntity.ok(response);
	}

	@GetMapping("{idCliente}/orders")
	public ResponseEntity<List<ClienteMetricaResponse.PedidoResumo>> listCustomerOrders(@PathVariable Long idCliente,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {
		List<ClienteMetricaResponse.PedidoResumo> pedidos = adminClienteMetricaUseCase.listarPedidosCliente(idCliente,
				dataInicio, dataFim);
		return ResponseEntity.ok(pedidos);
	}
}
