package br.com.postech.techchallange.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.postech.techchallange.adapter.in.rest.response.ClienteMetricaResponse;
import br.com.postech.techchallange.domain.model.Cliente;
import br.com.postech.techchallange.domain.model.Pedido;
import br.com.postech.techchallange.domain.model.PedidoProduto;
import br.com.postech.techchallange.domain.model.Produto;
import br.com.postech.techchallange.domain.model.StatusPedido;
import br.com.postech.techchallange.domain.port.in.GerenciarStatusPedidoUseCase;
import br.com.postech.techchallange.domain.port.out.CategoriaRepositoryPort;
import br.com.postech.techchallange.domain.port.out.ClienteRepositoryPort;
import br.com.postech.techchallange.domain.port.out.PedidoProdutoRepositoryPort;
import br.com.postech.techchallange.domain.port.out.PedidoRepositoryPort;
import br.com.postech.techchallange.domain.port.out.ProdutoRepositoryPort;

@ExtendWith(MockitoExtension.class)
class AdminClienteMetricaServiceTest {

	@Mock
	private PedidoRepositoryPort pedidoRepository;

	@Mock
	private ClienteRepositoryPort clienteRepository;

	@Mock
	private PedidoProdutoRepositoryPort pedidoProdutoRepositoryPort;

	@Mock
	private GerenciarStatusPedidoUseCase gerenciarStatusPedidoUseCase;

	@Mock
	private ProdutoRepositoryPort produtoRepositoryPort;

	@Mock
	private CategoriaRepositoryPort categoriaRepositoryPort;

	@InjectMocks
	private AdminClienteMetricaService service;

	private LocalDateTime now;

	@BeforeEach
	void setUp() {
		now = LocalDateTime.now();
	}

	@Test
	void deveRetornarMetricasDoClienteComPedidos() {
		Long clienteId = 1L;
		Cliente cliente = cliente(clienteId);
		Pedido pedido1 = pedido(1L, now.minusDays(1), 10L);
		Pedido pedido2 = pedido(2L, now, 10L);

		PedidoProduto pedidoProduto1 = pedidoProduto(BigDecimal.valueOf(10), 2);
		PedidoProduto pedidoProduto2 = pedidoProduto(BigDecimal.valueOf(5), 1);

		when(clienteRepository.buscarPorId(clienteId)).thenReturn(Optional.of(cliente));
		when(pedidoRepository.listarPorCliente(clienteId)).thenReturn(List.of(pedido1, pedido2));
		when(pedidoProdutoRepositoryPort.buscarPorIdPedido(1L)).thenReturn(List.of(pedidoProduto1));
		when(pedidoProdutoRepositoryPort.buscarPorIdPedido(2L)).thenReturn(List.of(pedidoProduto2));
		when(gerenciarStatusPedidoUseCase.buscarStatusPedidoPorId(10L)).thenReturn(new StatusPedido(10L, "FINALIZADO"));

		ClienteMetricaResponse response = service.obterMetricasCliente(clienteId);

		assertThat(response).isNotNull();
		assertThat(response.getIdCliente()).isEqualTo(clienteId);
		assertThat(response.getNome()).isEqualTo("João");
		assertThat(response.getEmail()).isEqualTo("joao@teste.com");
		assertThat(response.getTotalPedidos()).isEqualTo(2);
		assertThat(response.getTotalGasto()).isEqualTo(BigDecimal.valueOf(25));
		assertThat(response.getDataUltimoPedido()).isEqualTo(now);
		assertThat(response.getIdUltimoPedido()).isEqualTo(2L);
		assertThat(response.getPedidos()).hasSize(2);
		assertThat(response.getPedidos().get(0).getStatus()).isEqualTo("FINALIZADO");
	}

	@Test
	void deveRetornarMetricasParaClienteInexistente() {
		Long clienteId = 999L;
		Pedido pedido = pedido(1L, now.minusDays(3), null);
		PedidoProduto pedidoProduto = pedidoProduto(BigDecimal.valueOf(8), 1);

		when(clienteRepository.buscarPorId(clienteId)).thenReturn(Optional.empty());
		when(pedidoRepository.listarPorCliente(clienteId)).thenReturn(List.of(pedido));
		when(pedidoProdutoRepositoryPort.buscarPorIdPedido(1L)).thenReturn(List.of(pedidoProduto));

		ClienteMetricaResponse response = service.obterMetricasCliente(clienteId);

		assertThat(response).isNotNull();
		assertThat(response.getNome()).isEqualTo("Não identificado");
		assertThat(response.getTotalPedidos()).isEqualTo(1);
		assertThat(response.getTotalGasto()).isEqualTo(BigDecimal.valueOf(8));
	}
	
	@Test
	void deveListarPedidosDoClienteComStatus() {
		Long clienteId = 1L;
		Pedido pedido = pedido(1L, now, 10L);
		PedidoProduto pedidoProduto = pedidoProduto(BigDecimal.valueOf(10), 1);

		when(pedidoRepository.listarPorCliente(clienteId)).thenReturn(List.of(pedido));
		when(pedidoProdutoRepositoryPort.buscarPorIdPedido(1L)).thenReturn(List.of(pedidoProduto));
		when(gerenciarStatusPedidoUseCase.buscarStatusPedidoPorId(10L)).thenReturn(new StatusPedido(10L, "RECEBIDO"));

		List<ClienteMetricaResponse.PedidoResumo> result = service.listarPedidosCliente(clienteId);

		assertThat(result).hasSize(1);
		assertThat(result.get(0).getIdPedido()).isEqualTo(1L);
		assertThat(result.get(0).getStatus()).isEqualTo("RECEBIDO");
	}

	@Test
	void deveExportarPedidosClienteParaCsv() {
		Long clienteId = 1L;
		Pedido pedido = pedido(1L, now, 10L);
		PedidoProduto pedidoProduto = pedidoProduto(BigDecimal.valueOf(10), 1);

		when(pedidoRepository.listarPorCliente(clienteId)).thenReturn(List.of(pedido));
		when(pedidoProdutoRepositoryPort.buscarPorIdPedido(1L)).thenReturn(List.of(pedidoProduto));
		when(gerenciarStatusPedidoUseCase.buscarStatusPedidoPorId(10L)).thenReturn(new StatusPedido(10L, "FINALIZADO"));

		String csv = service.exportarPedidosClienteCsv(clienteId, null, null);

		assertThat(csv).contains("ID Pedido,Data Pedido,Valor Total,Status");
		assertThat(csv).contains("1");
		assertThat(csv).contains("FINALIZADO");
	}

	@Test
	void deveGerarRelatorioConversaoClientes() {
		Pedido identificado = pedido(1L, now, null); // com cliente
		Pedido anonimo = new Pedido();
		anonimo.setId(2L);
		anonimo.setDataPedido(now);

		when(pedidoRepository.listarPorCliente(null)).thenReturn(List.of(identificado, anonimo));

		var result = service.relatorioConversaoClientes(null, null);

		assertThat(result).isInstanceOf(Map.class);
		assertThat(result).containsEntry("totalPedidos", 2L);
		assertThat(result).containsEntry("pedidosIdentificados", 1L);
		assertThat(result).containsEntry("pedidosAnonimos", 1L);
		assertThat(result).containsKey("taxaConversao");
	}

	@Test
	void deveListarRankingDeClientesMaisAtivos() {
		Pedido pedido1 = pedido(1L, now, null);
		Pedido pedido2 = pedido(2L, now, null);
		pedido2.setIdCliente(1L); // mesmo cliente

		when(pedidoRepository.listarPorCliente(null)).thenReturn(List.of(pedido1, pedido2));
		when(clienteRepository.buscarPorId(1L)).thenReturn(Optional.of(cliente(1L)));

		List<?> ranking = service.rankingClientesMaisAtivos(null, null, 1);

		assertThat(ranking).hasSize(1);
	}

	@SuppressWarnings("deprecation")
	@Test
	void deveRetornarRelatorioDeVendasFiltradoPorProdutoECategoria() {
		Long produtoId = 101L;
		Long categoriaId = 202L;

		Pedido pedido = pedido(1L, now, null);
		PedidoProduto pedidoProduto = pedidoProduto(BigDecimal.valueOf(10), 2);
		pedidoProduto.setIdProduto(produtoId);

		when(pedidoRepository.listarPorCliente(null)).thenReturn(List.of(pedido));
		when(pedidoProdutoRepositoryPort.buscarPorIdPedido(1L)).thenReturn(List.of(pedidoProduto));
		when(produtoRepositoryPort.buscarPorId(produtoId)).thenReturn(Optional.of(new Produto(produtoId, "Hamburguer", "hamburguer", new BigDecimal("30"), null, categoriaId)));
		when(categoriaRepositoryPort.buscarPorId(categoriaId)).thenReturn(Optional.of(new br.com.postech.techchallange.domain.model.Categoria(categoriaId, "Lanche")));

		var resultado = service.relatorioVendasPorProdutoCategoria(null, null, produtoId, categoriaId);

		assertThat(resultado).asList().hasSize(1);
	}


	private Pedido pedido(Long id, LocalDateTime data, Long statusId) {
		Pedido p = new Pedido();
		p.setId(id);
		p.setDataPedido(data);
		p.setIdStatusPedido(statusId);
		p.setIdCliente(1L);
		return p;
	}

	private Cliente cliente(Long id) {
		Cliente c = new Cliente();
		c.setId(id);
		c.setNomeCliente("João");
		c.setEmailCliente("joao@teste.com");
		return c;
	}

	private PedidoProduto pedidoProduto(BigDecimal preco, int qtd) {
		PedidoProduto pp = new PedidoProduto();
		pp.setPrecoUnitario(preco);
		pp.setQuantidade(qtd);
		return pp;
	}
}
