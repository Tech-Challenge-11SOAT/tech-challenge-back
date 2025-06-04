package br.com.postech.techchallange.application.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.postech.techchallange.adapter.in.rest.response.ClienteMetricaResponse;
import br.com.postech.techchallange.adapter.in.rest.response.VendaProdutoCategoriaDTO;
import br.com.postech.techchallange.domain.model.Categoria;
import br.com.postech.techchallange.domain.model.Cliente;
import br.com.postech.techchallange.domain.model.Pedido;
import br.com.postech.techchallange.domain.model.PedidoProduto;
import br.com.postech.techchallange.domain.model.Produto;
import br.com.postech.techchallange.domain.port.in.AdminClienteMetricaUseCase;
import br.com.postech.techchallange.domain.port.in.GerenciarStatusPedidoUseCase;
import br.com.postech.techchallange.domain.port.out.CategoriaRepositoryPort;
import br.com.postech.techchallange.domain.port.out.ClienteRepositoryPort;
import br.com.postech.techchallange.domain.port.out.PedidoProdutoRepositoryPort;
import br.com.postech.techchallange.domain.port.out.PedidoRepositoryPort;
import br.com.postech.techchallange.domain.port.out.ProdutoRepositoryPort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminClienteMetricaService implements AdminClienteMetricaUseCase {

	private final PedidoRepositoryPort pedidoRepository;
	private final ClienteRepositoryPort clienteRepository;
	private final GerenciarStatusPedidoUseCase gerenciarStatusPedidoUseCase;
	private final PedidoProdutoRepositoryPort pedidoProdutoRepositoryPort;
	private final ProdutoRepositoryPort produtoRepositoryPort;
	private final CategoriaRepositoryPort categoriaRepositoryPort;

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
			pedidos = pedidos.stream()
					.filter(p -> p.getDataPedido() != null && !p.getDataPedido().isBefore(dataInicio))
					.collect(Collectors.toList());
		}
		if (dataFim != null) {
			pedidos = pedidos.stream()
					.filter(p -> p.getDataPedido() != null && !p.getDataPedido().isAfter(dataFim))
					.collect(Collectors.toList());
		}
		
		int totalPedidos = pedidos.size();
		BigDecimal totalGasto = pedidos.stream()
				.map(p -> this.calcularValorTotalPedido(p.getId()))
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		LocalDateTime dataUltimoPedido = pedidos.stream()
				.max(Comparator.comparing(Pedido::getDataPedido))
				.map(Pedido::getDataPedido)
				.orElse(null);

		Long idUltimoPedido = pedidos.stream()
				.max(Comparator.comparing(Pedido::getDataPedido))
				.map(Pedido::getId)
				.orElse(null);

		List<ClienteMetricaResponse.PedidoResumo> pedidosResumo = pedidos.stream().map(p -> {
			BigDecimal valor = calcularValorTotalPedido(p.getId());
			String status = "";

			if (p.getIdStatusPedido() != null) {
				status = gerenciarStatusPedidoUseCase.buscarStatusPedidoPorId(p.getIdStatusPedido()).getNomeStatus();
			}
			return ClienteMetricaResponse.PedidoResumo.builder()
					.idPedido(p.getId())
					.dataPedido(p.getDataPedido())
					.valorTotal(valor)
					.status(status)
					.build();

		}).collect(Collectors.toList());
		return ClienteMetricaResponse.builder()
				.idCliente(cliente != null ? cliente.getId() : null)
				.nome(cliente != null ? cliente.getNomeCliente() : "Não identificado")
				.email(cliente != null ? cliente.getEmailCliente() : null)
				.totalPedidos(totalPedidos)
				.totalGasto(totalGasto).dataUltimoPedido(dataUltimoPedido)
				.idUltimoPedido(idUltimoPedido)
				.pedidos(pedidosResumo)
				.build();
	}

	@Override
	public ClienteMetricaResponse obterMetricasCliente(Long idCliente) {
		return this.obterMetricasCliente(idCliente, null, null);
	}

	@Override
	public List<ClienteMetricaResponse.PedidoResumo> listarPedidosCliente(Long idCliente, LocalDateTime dataInicio, LocalDateTime dataFim) {
		List<Pedido> pedidos;
		if (idCliente == null) {
			pedidos = pedidoRepository.listarPorCliente(null);
		} else {
			pedidos = pedidoRepository.listarPorCliente(idCliente);
		}

		if (dataInicio != null) {
			pedidos = pedidos.stream()
					.filter(p -> p.getDataPedido() != null && !p.getDataPedido().isBefore(dataInicio))
					.collect(Collectors.toList());
		}
		if (dataFim != null) {
			pedidos = pedidos.stream()
					.filter(p -> p.getDataPedido() != null && !p.getDataPedido().isAfter(dataFim))
					.collect(Collectors.toList());
		}
		return pedidos.stream().map(p -> {
			BigDecimal valor = calcularValorTotalPedido(p.getId());
			String status = "";

			if (p.getIdStatusPedido() != null) {
				status = gerenciarStatusPedidoUseCase.buscarStatusPedidoPorId(p.getIdStatusPedido()).getNomeStatus();
			}
			return ClienteMetricaResponse.PedidoResumo.builder()
					.idPedido(p.getId())
					.dataPedido(p.getDataPedido())
					.valorTotal(valor)
					.status(status)
					.build();
		}).collect(Collectors.toList());
	}

	@Override
	public List<ClienteMetricaResponse.PedidoResumo> listarPedidosCliente(Long idCliente) {
		return this.listarPedidosCliente(idCliente, null, null);
	}

	@Override
	public String exportarPedidosClienteCsv(Long idCliente, LocalDateTime dataInicio, LocalDateTime dataFim) {
		List<ClienteMetricaResponse.PedidoResumo> pedidos = this.listarPedidosCliente(idCliente, dataInicio, dataFim);
		StringBuilder csv = new StringBuilder();

		csv.append("ID Pedido,Data Pedido,Valor Total,Status\n");
		pedidos.forEach(p -> csv.append(p.getIdPedido()).append(",").append(p.getDataPedido()).append(",").append(p.getValorTotal()).append(",").append(p.getStatus()).append("\n"));
		return csv.toString();
	}

	@Override
	public Object relatorioVendasPorProdutoCategoria(LocalDateTime dataInicio, LocalDateTime dataFim, Long idProduto, Long idCategoria) {
		List<Pedido> pedidos = pedidoRepository.listarPorCliente(null);
		pedidos = this.filtrarPedidosPorPeriodo(pedidos, dataInicio, dataFim);

		var resultado = pedidos.stream()
				.flatMap(p -> pedidoProdutoRepositoryPort.buscarPorIdPedido(p.getId()).stream()
						.filter(pp -> filtrarPorProdutoECategoria(pp, idProduto, idCategoria))
						.map(pp -> toVendaProdutoCategoriaDTO(pp, p)))
				.collect(Collectors.toList());
		return resultado;
	}

	private List<Pedido> filtrarPedidosPorPeriodo(List<Pedido> pedidos, LocalDateTime dataInicio,
			LocalDateTime dataFim) {
		return pedidos.stream().filter(
				p -> (dataInicio == null || (p.getDataPedido() != null && !p.getDataPedido().isBefore(dataInicio)))
						&& (dataFim == null || (p.getDataPedido() != null && !p.getDataPedido().isAfter(dataFim))))
				.collect(Collectors.toList());
	}

	private boolean filtrarPorProdutoECategoria(br.com.postech.techchallange.domain.model.PedidoProduto pp,
			Long idProduto, Long idCategoria) {
		if (idProduto != null && !pp.getIdProduto().equals(idProduto)) {
			return false;
		}
		if (idCategoria != null) {
			Produto prod = produtoRepositoryPort.buscarPorId(pp.getIdProduto()).orElse(null);
			if (prod == null || prod.getIdCategoria() == null || !prod.getIdCategoria().equals(idCategoria)) {
				return false;
			}
		}
		return true;
	}

	private VendaProdutoCategoriaDTO toVendaProdutoCategoriaDTO(PedidoProduto pedidoProduto, Pedido pedido) {
		Produto prod = produtoRepositoryPort.buscarPorId(pedidoProduto.getIdProduto()).orElse(null);
		String nomeProduto = prod != null
				? prod.getNome()
				: null;

		Long idCat = prod != null
				? prod.getIdCategoria()
				: null;

		String nomeCategoria = this.buscarNomeCategoria(idCat);

		return new VendaProdutoCategoriaDTO(
				pedidoProduto.getIdProduto(),
				nomeProduto,
				idCat,
				nomeCategoria,
				pedidoProduto.getQuantidade(),
				pedidoProduto.getPrecoUnitario().multiply(BigDecimal.valueOf(pedidoProduto.getQuantidade())), pedido.getDataPedido()
		);
	}

	private String buscarNomeCategoria(Long idCategoria) {
		if (idCategoria == null) {
			return null;
		}
		return categoriaRepositoryPort.buscarPorId(idCategoria)
				.map(Categoria::getNome).orElse(null);
	}

	@Override
	public Object rankingClientesMaisAtivos(LocalDateTime dataInicio, LocalDateTime dataFim, int limit) {
		List<Pedido> pedidos = pedidoRepository.listarPorCliente(null);
		if (dataInicio != null) {
			pedidos = pedidos.stream().filter(p -> p.getDataPedido() != null && !p.getDataPedido().isBefore(dataInicio))
					.collect(Collectors.toList());
		}
		if (dataFim != null) {
			pedidos = pedidos.stream().filter(p -> p.getDataPedido() != null && !p.getDataPedido().isAfter(dataFim))
					.collect(Collectors.toList());
		}

		var ranking = pedidos.stream().filter(p -> p.getIdCliente() != null)
				.collect(Collectors.groupingBy(Pedido::getIdCliente, Collectors.counting())).entrySet().stream()
				.sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue())).limit(limit)
				.collect(Collectors.toList());
		return ranking;
	}

	@Override
	public Object relatorioConversaoClientes(LocalDateTime dataInicio, LocalDateTime dataFim) {
		List<Pedido> pedidos = pedidoRepository.listarPorCliente(null);
		if (dataInicio != null) {
			pedidos = pedidos.stream().filter(p -> p.getDataPedido() != null && !p.getDataPedido().isBefore(dataInicio))
					.collect(Collectors.toList());
		}
		if (dataFim != null) {
			pedidos = pedidos.stream().filter(p -> p.getDataPedido() != null && !p.getDataPedido().isAfter(dataFim))
					.collect(Collectors.toList());
		}

		long totalPedidos = pedidos.size();
		long pedidosIdentificados = pedidos.stream().filter(p -> p.getIdCliente() != null).count();
		long pedidosAnonimos = totalPedidos - pedidosIdentificados;
		double taxaConversao = totalPedidos > 0 ? (double) pedidosIdentificados / totalPedidos : 0.0;

		return Map.of(
				"totalPedidos", totalPedidos,
				"pedidosIdentificados", pedidosIdentificados,
				"pedidosAnonimos", pedidosAnonimos,
				"taxaConversao", taxaConversao
		);
	}

	private BigDecimal calcularValorTotalPedido(Long idPedido) {
		return pedidoProdutoRepositoryPort.buscarPorIdPedido(idPedido).stream()
				.map(prod -> prod.getPrecoUnitario().multiply(BigDecimal.valueOf(prod.getQuantidade())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}
