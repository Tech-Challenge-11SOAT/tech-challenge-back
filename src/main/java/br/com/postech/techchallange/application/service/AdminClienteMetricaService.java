package br.com.postech.techchallange.application.service;

import br.com.postech.techchallange.adapter.in.rest.response.ClienteMetricaResponse;
import br.com.postech.techchallange.domain.model.Pedido;
import br.com.postech.techchallange.domain.model.Cliente;
import br.com.postech.techchallange.domain.port.in.AdminClienteMetricaUseCase;
import br.com.postech.techchallange.domain.port.out.PedidoRepositoryPort;
import br.com.postech.techchallange.domain.port.out.ClienteRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminClienteMetricaService implements AdminClienteMetricaUseCase {

	private final PedidoRepositoryPort pedidoRepository;
	private final ClienteRepositoryPort clienteRepository;
	private final br.com.postech.techchallange.domain.port.in.GerenciarStatusPedidoUseCase gerenciarStatusPedidoUseCase;
	private final br.com.postech.techchallange.domain.port.out.PedidoProdutoRepositoryPort pedidoProdutoRepositoryPort;

	@Override
	public ClienteMetricaResponse obterMetricasCliente(Long idCliente, LocalDateTime dataInicio, LocalDateTime dataFim) {
		List<Pedido> pedidos;
		Cliente cliente = null;
		if (idCliente == null) {
			pedidos = pedidoRepository.listarPorCliente(null);
		} else {
			cliente = clienteRepository.buscarPorId(idCliente).orElse(null);
			pedidos = pedidoRepository.listarPorCliente(idCliente);
		}

		if (dataInicio != null) {
			pedidos = pedidos.stream().filter(p -> p.getDataPedido() != null && !p.getDataPedido().isBefore(dataInicio))
					.collect(Collectors.toList());
		}
		if (dataFim != null) {
			pedidos = pedidos.stream().filter(p -> p.getDataPedido() != null && !p.getDataPedido().isAfter(dataFim))
					.collect(Collectors.toList());
		}
		int totalPedidos = pedidos.size();
		BigDecimal totalGasto = pedidos.stream().map(p -> calcularValorTotalPedido(p.getId())).reduce(BigDecimal.ZERO,
				BigDecimal::add);

		LocalDateTime dataUltimoPedido = pedidos.stream().max(Comparator.comparing(Pedido::getDataPedido))
				.map(Pedido::getDataPedido).orElse(null);

		Long idUltimoPedido = pedidos.stream().max(Comparator.comparing(Pedido::getDataPedido)).map(Pedido::getId)
				.orElse(null);

		List<ClienteMetricaResponse.PedidoResumo> pedidosResumo = pedidos.stream().map(p -> {
			BigDecimal valor = calcularValorTotalPedido(p.getId());
			String status = "";
			if (p.getIdStatusPedido() != null) {
				status = gerenciarStatusPedidoUseCase.buscarStatusPedidoPorId(p.getIdStatusPedido()).getNomeStatus();
			}
			return ClienteMetricaResponse.PedidoResumo.builder().idPedido(p.getId()).dataPedido(p.getDataPedido())
					.valorTotal(valor).status(status).build();
		}).collect(Collectors.toList());
		return ClienteMetricaResponse.builder().idCliente(cliente != null ? cliente.getId() : null)
				.nome(cliente != null ? cliente.getNomeCliente() : "Não identificado")
				.email(cliente != null ? cliente.getEmailCliente() : null).totalPedidos(totalPedidos)
				.totalGasto(totalGasto).dataUltimoPedido(dataUltimoPedido).idUltimoPedido(idUltimoPedido)
				.pedidos(pedidosResumo).build();
	}

	@Override
	public ClienteMetricaResponse obterMetricasCliente(Long idCliente) {
		return obterMetricasCliente(idCliente, null, null);
	}

	@Override
	public List<ClienteMetricaResponse.PedidoResumo> listarPedidosCliente(Long idCliente, LocalDateTime dataInicio, LocalDateTime dataFim) {
		List<Pedido> pedidos;
		if (idCliente == null) {
			pedidos = pedidoRepository.listarPorCliente(null);
		} else {
			pedidos = pedidoRepository.listarPorCliente(idCliente);
		}
		// Filtrar por período, se informado
		if (dataInicio != null) {
			pedidos = pedidos.stream().filter(p -> p.getDataPedido() != null && !p.getDataPedido().isBefore(dataInicio))
					.collect(Collectors.toList());
		}
		if (dataFim != null) {
			pedidos = pedidos.stream().filter(p -> p.getDataPedido() != null && !p.getDataPedido().isAfter(dataFim))
					.collect(Collectors.toList());
		}
		return pedidos.stream().map(p -> {
			BigDecimal valor = calcularValorTotalPedido(p.getId());
			String status = "";
			if (p.getIdStatusPedido() != null) {
				status = gerenciarStatusPedidoUseCase.buscarStatusPedidoPorId(p.getIdStatusPedido()).getNomeStatus();
			}
			return ClienteMetricaResponse.PedidoResumo.builder().idPedido(p.getId()).dataPedido(p.getDataPedido())
					.valorTotal(valor).status(status).build();
		}).collect(Collectors.toList());
	}

	@Override
	public List<ClienteMetricaResponse.PedidoResumo> listarPedidosCliente(Long idCliente) {
		return listarPedidosCliente(idCliente, null, null);
	}

	@Override
	public String exportarPedidosClienteCsv(Long idCliente, LocalDateTime dataInicio, LocalDateTime dataFim) {
		List<ClienteMetricaResponse.PedidoResumo> pedidos = listarPedidosCliente(idCliente, dataInicio, dataFim);
		StringBuilder csv = new StringBuilder();
		csv.append("ID Pedido,Data Pedido,Valor Total,Status\n");
		pedidos.forEach(p -> csv.append(p.getIdPedido())
				.append(",")
				.append(p.getDataPedido())
				.append(",")
				.append(p.getValorTotal())
				.append(",")
				.append(p.getStatus())
				.append("\n"));
		return csv.toString();
	}

	private BigDecimal calcularValorTotalPedido(Long idPedido) {
		return pedidoProdutoRepositoryPort.buscarPorIdPedido(idPedido).stream()
				.map(prod -> prod.getPrecoUnitario().multiply(BigDecimal.valueOf(prod.getQuantidade())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}
