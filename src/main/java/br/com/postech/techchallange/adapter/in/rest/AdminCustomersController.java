package br.com.postech.techchallange.adapter.in.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

	@Operation(summary = "Obter métricas de clientes não identificados", description = "Retorna métricas agregadas dos pedidos realizados por clientes anônimos (não identificados). Permite filtrar por período.", responses = {
			@ApiResponse(responseCode = "200", description = "Métricas retornadas com sucesso"),
			@ApiResponse(responseCode = "401", description = "Não autorizado") })
	@Parameters({
			@Parameter(name = "dataInicio", description = "Data/hora inicial do filtro (formato ISO 8601)", required = false, example = "2024-01-01T00:00:00"),
			@Parameter(name = "dataFim", description = "Data/hora final do filtro (formato ISO 8601)", required = false, example = "2024-12-31T23:59:59") })
	@GetMapping("metrics")
	public ResponseEntity<ClienteMetricaResponse> getAnonymousCustomerMetrics(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {
		ClienteMetricaResponse response = adminClienteMetricaUseCase.obterMetricasCliente(null, dataInicio, dataFim);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "Listar pedidos de clientes não identificados", description = "Retorna a lista de pedidos realizados por clientes anônimos (não identificados). Permite filtrar por período.", responses = {
			@ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso"),
			@ApiResponse(responseCode = "401", description = "Não autorizado") })
	@Parameters({
			@Parameter(name = "dataInicio", description = "Data/hora inicial do filtro (formato ISO 8601)", required = false, example = "2024-01-01T00:00:00"),
			@Parameter(name = "dataFim", description = "Data/hora final do filtro (formato ISO 8601)", required = false, example = "2024-12-31T23:59:59") })
	@GetMapping("orders")
	public ResponseEntity<List<ClienteMetricaResponse.PedidoResumo>> listAnonymousCustomerOrders(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {
		List<ClienteMetricaResponse.PedidoResumo> pedidos = adminClienteMetricaUseCase.listarPedidosCliente(null,
				dataInicio, dataFim);
		return ResponseEntity.ok(pedidos);
	}

	@Operation(summary = "Obter métricas de um cliente identificado", description = "Retorna métricas agregadas dos pedidos realizados por um cliente identificado. Permite filtrar por período.", responses = {
			@ApiResponse(responseCode = "200", description = "Métricas retornadas com sucesso"),
			@ApiResponse(responseCode = "401", description = "Não autorizado") })
	@Parameters({
			@Parameter(name = "idCliente", description = "ID do cliente identificado", required = true, example = "1"),
			@Parameter(name = "dataInicio", description = "Data/hora inicial do filtro (formato ISO 8601)", required = false, example = "2024-01-01T00:00:00"),
			@Parameter(name = "dataFim", description = "Data/hora final do filtro (formato ISO 8601)", required = false, example = "2024-12-31T23:59:59") })
	@GetMapping("{idCliente}/metrics")
	public ResponseEntity<ClienteMetricaResponse> getCustomerMetrics(@PathVariable Long idCliente,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {
		ClienteMetricaResponse response = adminClienteMetricaUseCase.obterMetricasCliente(idCliente, dataInicio,
				dataFim);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "Listar pedidos de um cliente identificado", description = "Retorna a lista de pedidos realizados por um cliente identificado. Permite filtrar por período.", responses = {
			@ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso"),
			@ApiResponse(responseCode = "401", description = "Não autorizado") })
	@Parameters({
			@Parameter(name = "idCliente", description = "ID do cliente identificado", required = true, example = "1"),
			@Parameter(name = "dataInicio", description = "Data/hora inicial do filtro (formato ISO 8601)", required = false, example = "2024-01-01T00:00:00"),
			@Parameter(name = "dataFim", description = "Data/hora final do filtro (formato ISO 8601)", required = false, example = "2024-12-31T23:59:59") })
	@GetMapping("{idCliente}/orders")
	public ResponseEntity<List<ClienteMetricaResponse.PedidoResumo>> listCustomerOrders(@PathVariable Long idCliente,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {
		List<ClienteMetricaResponse.PedidoResumo> pedidos = adminClienteMetricaUseCase.listarPedidosCliente(idCliente,
				dataInicio, dataFim);
		return ResponseEntity.ok(pedidos);
	}

	@Operation(summary = "Exportar pedidos do cliente em CSV", description = "Exporta todos os pedidos de um cliente identificado em formato CSV. Permite filtrar por período de data de pedido.", responses = {
			@ApiResponse(responseCode = "200", description = "Arquivo CSV gerado com sucesso", content = @Content(mediaType = "text/csv")),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content) })
	@Parameters({
			@Parameter(name = "idCliente", description = "ID do cliente identificado", required = true, example = "1"),
			@Parameter(name = "dataInicio", description = "Data/hora inicial do filtro (formato ISO 8601)", required = false, example = "2024-01-01T00:00:00"),
			@Parameter(name = "dataFim", description = "Data/hora final do filtro (formato ISO 8601)", required = false, example = "2024-12-31T23:59:59") })
	@GetMapping("{idCliente}/orders/export")
	public ResponseEntity<byte[]> exportCustomerOrdersCsv(@PathVariable Long idCliente,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {
		String csv = adminClienteMetricaUseCase.exportarPedidosClienteCsv(idCliente, dataInicio, dataFim);
		byte[] csvBytes = csv.getBytes(StandardCharsets.UTF_8);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("text/csv"));
		headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=pedidos_cliente_" + idCliente + ".csv");
		return ResponseEntity.ok().headers(headers).body(csvBytes);
	}

	@Operation(summary = "Exportar pedidos de clientes não identificados em CSV", description = "Exporta todos os pedidos de clientes anônimos (não identificados) em formato CSV. Permite filtrar por período de data de pedido.", responses = {
			@ApiResponse(responseCode = "200", description = "Arquivo CSV gerado com sucesso", content = @Content(mediaType = "text/csv")),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content) })
	@Parameters({
			@Parameter(name = "dataInicio", description = "Data/hora inicial do filtro (formato ISO 8601)", required = false, example = "2024-01-01T00:00:00"),
			@Parameter(name = "dataFim", description = "Data/hora final do filtro (formato ISO 8601)", required = false, example = "2024-12-31T23:59:59") })
	@GetMapping("orders/export")
	public ResponseEntity<byte[]> exportAnonymousOrdersCsv(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {
		String csv = adminClienteMetricaUseCase.exportarPedidosClienteCsv(null, dataInicio, dataFim);
		byte[] csvBytes = csv.getBytes(StandardCharsets.UTF_8);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("text/csv"));
		headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=pedidos_anonimos.csv");
		return ResponseEntity.ok().headers(headers).body(csvBytes);
	}

	@Operation(summary = "Relatório de vendas por produto/categoria/período", description = "Retorna o total vendido e quantidade por produto e categoria, filtrando por período.")
	@GetMapping("reports/sales")
	public ResponseEntity<?> getSalesReport(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim,
			@RequestParam(required = false) Long idProduto, @RequestParam(required = false) Long idCategoria) {
		return ResponseEntity.ok(adminClienteMetricaUseCase.relatorioVendasPorProdutoCategoria(dataInicio, dataFim,
				idProduto, idCategoria));
	}

	@Operation(summary = "Ranking de clientes mais ativos", description = "Retorna o ranking dos clientes que mais fizeram pedidos no período.")
	@GetMapping("reports/top-customers")
	public ResponseEntity<?> getTopCustomers(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim,
			@RequestParam(defaultValue = "10") int limit) {
		return ResponseEntity.ok(adminClienteMetricaUseCase.rankingClientesMaisAtivos(dataInicio, dataFim, limit));
	}

	@Operation(summary = "Relatório de conversão de clientes anônimos para identificados", description = "Mostra quantos clientes anônimos se tornaram identificados em determinado período.")
	@GetMapping("reports/conversion")
	public ResponseEntity<?> getConversionReport(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {
		return ResponseEntity.ok(adminClienteMetricaUseCase.relatorioConversaoClientes(dataInicio, dataFim));
	}
}
