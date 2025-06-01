package br.com.postech.techchallange.application.service;

import br.com.postech.techchallange.adapter.in.rest.response.PedidoCompletoResponse;
import br.com.postech.techchallange.domain.model.Pedido;
import br.com.postech.techchallange.domain.port.out.PedidoRepositoryPort;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GerenciarPedidoServiceTest {

	private PedidoRepositoryPort repository;
	private GerenciarPedidoService service;

	@BeforeEach
	void setUp() {
		repository = mock(PedidoRepositoryPort.class);
		service = new GerenciarPedidoService(repository);
	}

	// Testes para salvar informações
	@Test
	@DisplayName("Deve criar um pedido com sucesso")
	void deveCriarPedidoComSucesso() {
		Pedido pedido = criarPedido(1L);
		when(repository.salvar(pedido)).thenReturn(pedido);

		Pedido resultado = service.criarPedido(pedido);

		assertNotNull(resultado);
		assertEquals(pedido.getId(), resultado.getId());
		verify(repository).salvar(pedido);
	}

	@Test
	@DisplayName("Deve lançar excecao ao tentar salvar pedido e ocorrer falha")
	void deveFalharAoCriarPedido() {
		Pedido pedido = criarPedido(1L);
		when(repository.salvar(pedido)).thenThrow(new RuntimeException("Erro ao salvar"));

		assertThrows(RuntimeException.class, () -> service.criarPedido(pedido));
		verify(repository).salvar(pedido);
	}

	// Testes para buscar pedido por ID
	@Test
	@DisplayName("Deve retornar o pedido pelo ID")
	void deveBuscarPedidoPorIdComSucesso() {
		Pedido pedido = criarPedido(1L);
		when(repository.buscarPorId(1L)).thenReturn(Optional.of(pedido));

		Pedido resultado = service.buscarPedido(1L);

		assertNotNull(resultado);
		assertEquals(pedido.getId(), resultado.getId());
		verify(repository).buscarPorId(1L);
	}

	@Test
    @DisplayName("Deve lançar exceção se pedido não for encontrado pelo ID")
    void deveLancarExcecaoSePedidoNaoForEncontrado() {
        when(repository.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.buscarPedido(99L));
        verify(repository).buscarPorId(99L);
    }

	// Testes para listar os pedidos
	@Test
	@DisplayName("Deve listar todos os pedidos")
	void deveListarTodosOsPedidos() {
		PedidoCompletoResponse pedido1 = criarPedidoCompletoResponse(1L);
		PedidoCompletoResponse pedido2 = criarPedidoCompletoResponse(2L);
		when(repository.listarTodos()).thenReturn(List.of(pedido1, pedido2));

		List<PedidoCompletoResponse> pedidos = service.listarPedidos();

		assertEquals(2, pedidos.size());
		verify(repository).listarTodos();
	}

	@Test
    @DisplayName("Deve lançar exceção se não houver pedidos")
    void deveLancarExcecaoSeNaoHouverPedidos() {
        when(repository.listarTodos()).thenReturn(List.of());

        assertThrows(EntityNotFoundException.class, () -> service.listarPedidos());
        verify(repository).listarTodos();
    }

	@Test
	@DisplayName("Deve listar pedidos por status")
	void deveListarPedidosPorStatus() {
		PedidoCompletoResponse pedido1 = criarPedidoCompletoResponse(1L);
		PedidoCompletoResponse pedido2 = criarPedidoCompletoResponse(2L);
		when(repository.listarPorStatus(1L)).thenReturn(List.of(pedido1, pedido2));

		List<PedidoCompletoResponse> pedidos = service.listarPorStatus(1L);

		assertEquals(2, pedidos.size());
		verify(repository).listarPorStatus(1L);
	}

	@Test
    @DisplayName("Deve lançar exceção se não houver pedidos com o status")
	void deveLancarExcecaoSeNaoHouverPedidosComStatus() {
	    when(repository.listarPorStatus(1L)).thenReturn(List.of());
	
	    assertThrows(EntityNotFoundException.class, () -> service.listarPorStatus(1L));
	    verify(repository).listarPorStatus(1L);
	}

	private Pedido criarPedido(Long id) {
		return new Pedido(id, 10L, LocalDateTime.now(), 1L, LocalDateTime.now(), 123 // valor fictício para filaPedido
		);
	}

	private PedidoCompletoResponse criarPedidoCompletoResponse(Long id) {
		PedidoCompletoResponse.Cliente cliente = new PedidoCompletoResponse.Cliente(10L, "Joao da Silva",
				"joao@email.com");

		PedidoCompletoResponse.StatusPedido status = new PedidoCompletoResponse.StatusPedido(1L, "Recebido");

		return new PedidoCompletoResponse(id, LocalDateTime.now(), LocalDateTime.now(), cliente, status, 123L);
	}

}
