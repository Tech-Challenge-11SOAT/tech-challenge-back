package br.com.postech.techchallange.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.postech.techchallange.adapter.in.rest.request.AtualizarPedidoRequest;
import br.com.postech.techchallange.adapter.in.rest.request.PedidoPagamentoRequest;
import br.com.postech.techchallange.adapter.in.rest.response.PedidoPagamentoResponse;
import br.com.postech.techchallange.domain.enums.StatusPagamentoEnum;
import br.com.postech.techchallange.domain.enums.StatusPedidoEnum;
import br.com.postech.techchallange.domain.model.Cliente;
import br.com.postech.techchallange.domain.model.Pagamento;
import br.com.postech.techchallange.domain.model.Pedido;
import br.com.postech.techchallange.domain.model.Produto;
import br.com.postech.techchallange.domain.model.StatusPagamento;
import br.com.postech.techchallange.domain.model.StatusPedido;
import br.com.postech.techchallange.domain.port.in.GerenciarClienteUseCase;
import br.com.postech.techchallange.domain.port.in.GerenciarPedidoUseCase;
import br.com.postech.techchallange.domain.port.in.GerenciarStatusPagamentoUseCase;
import br.com.postech.techchallange.domain.port.in.GerenciarStatusPedidoUseCase;
import br.com.postech.techchallange.domain.port.in.PagamentoValidatorPort;
import br.com.postech.techchallange.domain.port.out.PagamentoRepositoryPort;
import br.com.postech.techchallange.domain.port.out.PedidoProdutoRepositoryPort;
import br.com.postech.techchallange.domain.port.out.ProdutoRepositoryPort;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class OrquestradorPedidoPagamentoServiceTest {

    @Mock
    private GerenciarPedidoUseCase gerenciarPedido;

    @Mock
    private GerenciarStatusPedidoUseCase gerenciarStatusPedido;

    @Mock
    private PedidoProdutoRepositoryPort pedidoProdutoRepository;

    @Mock
    private ProdutoRepositoryPort produtoRepository;

    @Mock
    private PagamentoRepositoryPort pagamentoRepository;

    @Mock
    private GerenciarStatusPagamentoUseCase gerenciarStatusPagamento;

    @Mock
    private PagamentoValidatorPort pagamentoValidator;

    @Mock
    private GerenciarClienteUseCase gerenciarClienteUseCase;

    @InjectMocks
    private OrquestradorPedidoPagamentoService service;

    @Captor
    private ArgumentCaptor<Pagamento> pagamentoCaptor;

    private Pedido pedidoTest;
    private Pagamento pagamentoTest;
    private StatusPedido statusPedidoTest;
    private StatusPagamento statusPagamentoTest;

    @BeforeEach
    void setUp() {
        pedidoTest = new Pedido(1L, 1L, LocalDateTime.now(), 1L, LocalDateTime.now(), 1);
        pagamentoTest = Pagamento.builder()
                .id(1L)
                .idPedido(1L)
                .metodoPagamento("PIX")
                .valorTotal(BigDecimal.valueOf(50.0))
                .dataPagamento(LocalDateTime.now())
                .idStatusPagamento(1L)
                .build();

        statusPedidoTest = StatusPedido.builder()
                .idStatusPedido(1L)
                .nomeStatus(StatusPedidoEnum.RECEBIDO.getStatus())
                .build();

        statusPagamentoTest = StatusPagamento.builder()
                .idStatusPagamento(1L)
                .nomeStatus(StatusPagamentoEnum.PENDENTE)
                .build();
    }

    @ParameterizedTest
    @MethodSource("statusPedidoProvider")
    @DisplayName("Deve atualizar pagamento corretamente com base no status do pedido")
    void deveAtualizarPedidoPagamentoComSucesso(StatusPedidoEnum statusPedidoEnum, StatusPagamentoEnum statusPagamentoEsperado) {
        // Arrange
        AtualizarPedidoRequest request = new AtualizarPedidoRequest();
        request.setId(1L);
        request.setStatus(statusPedidoEnum);

        StatusPagamento statusPagamentoMock = StatusPagamento.builder()
                .idStatusPagamento(99L)
                .nomeStatus(statusPagamentoEsperado)
                .build();

        when(gerenciarPedido.buscarPedido(1L)).thenReturn(pedidoTest);
        when(gerenciarStatusPedido.buscarStatusPedidoPorNome(statusPedidoEnum.getStatus()))
                .thenReturn(statusPedidoTest);
        when(gerenciarPedido.atualizarPedido(any(), any())).thenReturn(pedidoTest);
        when(pagamentoRepository.buscarPorId(1L)).thenReturn(Optional.of(pagamentoTest));
        when(gerenciarStatusPagamento.buscarStatusPagamentoPorStatus(statusPagamentoEsperado.getStatus()))
                .thenReturn(statusPagamentoMock);
        when(pagamentoRepository.salvar(any())).thenReturn(pagamentoTest);

        // Act
        Pedido resultado = service.atualizarPedidoPagamento(request);

        // Assert
        assertNotNull(resultado);
        verify(pagamentoRepository).salvar(pagamentoCaptor.capture());
        assertEquals(statusPagamentoMock.getIdStatusPagamento(), pagamentoCaptor.getValue().getIdStatusPagamento());
    }

    private static Stream<Arguments> statusPedidoProvider() {
        return Stream.of(
                Arguments.of(StatusPedidoEnum.RECEBIDO_NAO_PAGO, StatusPagamentoEnum.PENDENTE),
                Arguments.of(StatusPedidoEnum.RECEBIDO, StatusPagamentoEnum.FINALIZADO),
                Arguments.of(StatusPedidoEnum.EM_ANDAMENTO, StatusPagamentoEnum.FINALIZADO),
                Arguments.of(StatusPedidoEnum.FINALIZADO, StatusPagamentoEnum.FINALIZADO),
                Arguments.of(StatusPedidoEnum.CANCELADO, StatusPagamentoEnum.ERRO) // default
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando pedido não for encontrado")
    void deveLancarExcecaoQuandoPedidoNaoEncontrado() {
        // Arrange
        AtualizarPedidoRequest request = new AtualizarPedidoRequest();
        request.setId(999L);
        request.setStatus(StatusPedidoEnum.RECEBIDO);

        when(gerenciarPedido.buscarPedido(999L)).thenReturn(null);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> service.atualizarPedidoPagamento(request));
        assertEquals("Pedido não encontrado", exception.getMessage());

        verify(gerenciarStatusPedido, never()).buscarStatusPedidoPorNome(any());
        verify(pagamentoRepository, never()).buscarPorId(any());
        verify(pagamentoRepository, never()).salvar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando pagamento não for encontrado")
    void deveLancarExcecaoQuandoPagamentoNaoEncontrado() {
        // Arrange
        AtualizarPedidoRequest request = new AtualizarPedidoRequest();
        request.setId(1L);
        request.setStatus(StatusPedidoEnum.RECEBIDO);

        when(gerenciarPedido.buscarPedido(1L)).thenReturn(pedidoTest);
        when(gerenciarStatusPedido.buscarStatusPedidoPorNome(any())).thenReturn(statusPedidoTest);
        when(gerenciarPedido.atualizarPedido(any(), any())).thenReturn(pedidoTest);
        when(pagamentoRepository.buscarPorId(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> service.atualizarPedidoPagamento(request));
        assertEquals("Pagamento não encontrado para o pedido", exception.getMessage());

        verify(pagamentoRepository, never()).salvar(any());
    }

    @Test
    @DisplayName("Deve orquestrar pedido e pagamento com sucesso")
    void deveOrquestrarPedidoPagamentoComSucesso() {
        // Arrange
        PedidoPagamentoRequest request = new PedidoPagamentoRequest();
        request.setIdCliente(1L);
        request.setMetodoPagamento("PIX");
        request.setProdutos(Arrays.asList(new PedidoPagamentoRequest.ItemProdutoRequest(1L, 2)));

        Cliente cliente = new Cliente(1L, "Cliente Teste", "test@test.com", "12345678901");

        Produto produto = Produto.builder()
                .id(1L)
                .preco(BigDecimal.TEN)
                .build();

        when(gerenciarClienteUseCase.buscarCliente(1L)).thenReturn(cliente);
        when(gerenciarStatusPedido.buscarStatusPedidoPorNome(StatusPedidoEnum.RECEBIDO_NAO_PAGO.getStatus()))
                .thenReturn(statusPedidoTest);
        when(gerenciarPedido.criarPedido(any())).thenReturn(pedidoTest);
        when(produtoRepository.buscarPorId(1L)).thenReturn(Optional.of(produto));
        when(gerenciarStatusPagamento.buscarStatusPagamentoPorStatus(StatusPagamentoEnum.PENDENTE.getStatus()))
                .thenReturn(statusPagamentoTest);
        when(pagamentoRepository.salvar(any())).thenReturn(pagamentoTest);

        // Act
        PedidoPagamentoResponse resultado = service.orquestrarPedidoPagamento(request);

        // Assert
        assertNotNull(resultado);
        assertEquals(pedidoTest.getId(), resultado.getIdPedido());
        assertEquals(pagamentoTest.getId(), resultado.getIdPagamento());
        assertEquals(request.getMetodoPagamento(), resultado.getMetodoPagamento());
        assertEquals(StatusPedidoEnum.RECEBIDO_NAO_PAGO.getStatus(), resultado.getStatus());
        assertEquals(pedidoTest.getFilaPedido(), resultado.getNumeroPedido());

        verify(pedidoProdutoRepository).salvarItemPedido(any());
        verify(pagamentoValidator).validar(any(), any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando cliente não for encontrado")
    void deveLancarExcecaoQuandoClienteNaoEncontrado() {
        // Arrange
        PedidoPagamentoRequest request = new PedidoPagamentoRequest();
        request.setIdCliente(999L);
        request.setMetodoPagamento("PIX");
        request.setProdutos(Arrays.asList(new PedidoPagamentoRequest.ItemProdutoRequest(1L, 2)));

        when(gerenciarClienteUseCase.buscarCliente(999L)).thenReturn(null);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> service.orquestrarPedidoPagamento(request));
        assertEquals("Cliente não encontrado para o id: 999", exception.getMessage());

        verify(gerenciarPedido, never()).criarPedido(any());
        verify(produtoRepository, never()).buscarPorId(any());
        verify(pagamentoRepository, never()).salvar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando produto não for encontrado")
    void deveLancarExcecaoQuandoProdutoNaoEncontrado() {
        // Arrange
        PedidoPagamentoRequest request = new PedidoPagamentoRequest();
        request.setIdCliente(1L);
        request.setMetodoPagamento("PIX");
        request.setProdutos(Arrays.asList(new PedidoPagamentoRequest.ItemProdutoRequest(999L, 2)));
        StatusPedido statusPedidoNaoPago = StatusPedido.builder()
                .idStatusPedido(1L)
                .nomeStatus(StatusPedidoEnum.RECEBIDO_NAO_PAGO.getStatus())
                .build();

        Cliente cliente = new Cliente(1L, "Cliente Teste", "test@test.com", "12345678901");
        when(gerenciarClienteUseCase.buscarCliente(1L)).thenReturn(cliente);
        when(gerenciarStatusPedido.buscarStatusPedidoPorNome(StatusPedidoEnum.RECEBIDO_NAO_PAGO.getStatus()))
                .thenReturn(statusPedidoNaoPago);
        when(gerenciarPedido.criarPedido(any())).thenReturn(pedidoTest);
        when(produtoRepository.buscarPorId(999L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> service.orquestrarPedidoPagamento(request));
        assertEquals("Produto não encontrado", exception.getMessage());

        verify(pagamentoRepository, never()).salvar(any());
    }

    @Test
    @DisplayName("Deve verificar ordem de execução ao orquestrar pedido com sucesso")
    void deveVerificarOrdemExecucaoAoOrquestrarPedido() {
        // Arrange
        PedidoPagamentoRequest request = new PedidoPagamentoRequest();
        request.setIdCliente(1L);
        request.setMetodoPagamento("PIX");
        request.setProdutos(Arrays.asList(createItemProdutoRequest(1L, 2)));

        Produto produto = Produto.builder()
                .id(1L)
                .preco(BigDecimal.TEN)
                .build();

        StatusPedido statusPedidoNaoPago = StatusPedido.builder()
                .idStatusPedido(1L)
                .nomeStatus(StatusPedidoEnum.RECEBIDO_NAO_PAGO.getStatus())
                .build();

        StatusPagamento statusPagamentoPendente = StatusPagamento.builder()
                .idStatusPagamento(1L)
                .nomeStatus(StatusPagamentoEnum.PENDENTE)
                .build();

        Cliente cliente = new Cliente(1L, "Cliente Teste", "test@test.com", "12345678901");

        when(gerenciarClienteUseCase.buscarCliente(1L)).thenReturn(cliente);
        when(gerenciarStatusPedido.buscarStatusPedidoPorNome(StatusPedidoEnum.RECEBIDO_NAO_PAGO.getStatus()))
                .thenReturn(statusPedidoNaoPago);
        when(gerenciarPedido.criarPedido(any())).thenReturn(pedidoTest);
        when(produtoRepository.buscarPorId(1L)).thenReturn(Optional.of(produto));
        when(gerenciarStatusPagamento.buscarStatusPagamentoPorStatus(StatusPagamentoEnum.PENDENTE.getStatus()))
                .thenReturn(statusPagamentoPendente);
        when(pagamentoRepository.salvar(any())).thenReturn(pagamentoTest);

        // Act
        service.orquestrarPedidoPagamento(request);

        // Assert
        InOrder inOrder = inOrder(gerenciarClienteUseCase, gerenciarStatusPedido, gerenciarPedido,
                produtoRepository, pedidoProdutoRepository, pagamentoValidator, pagamentoRepository);

        inOrder.verify(gerenciarClienteUseCase).buscarCliente(1L);
        inOrder.verify(gerenciarStatusPedido).buscarStatusPedidoPorNome(StatusPedidoEnum.RECEBIDO_NAO_PAGO.getStatus());
        inOrder.verify(gerenciarPedido).criarPedido(any());
        inOrder.verify(produtoRepository).buscarPorId(1L);
        inOrder.verify(pedidoProdutoRepository).salvarItemPedido(any());
        inOrder.verify(pagamentoValidator).validar(any(), any());
        inOrder.verify(pagamentoRepository).salvar(any());
    }

    @Test
    @DisplayName("Deve validar cálculo do valor total ao orquestrar pedido")
    void deveValidarCalculoValorTotal() {
        // Arrange
        PedidoPagamentoRequest request = new PedidoPagamentoRequest();
        request.setIdCliente(1L);
        request.setMetodoPagamento("PIX");
        request.setProdutos(Arrays.asList(
                createItemProdutoRequest(1L, 2),
                createItemProdutoRequest(2L, 3)
        ));

        Produto produto1 = Produto.builder()
                .id(1L)
                .preco(BigDecimal.valueOf(10.0))
                .build();

        Produto produto2 = Produto.builder()
                .id(2L)
                .preco(BigDecimal.valueOf(20.0))
                .build();

        StatusPedido statusPedidoNaoPago = StatusPedido.builder()
                .idStatusPedido(1L)
                .nomeStatus(StatusPedidoEnum.RECEBIDO_NAO_PAGO.getStatus())
                .build();

        StatusPagamento statusPagamentoPendente = StatusPagamento.builder()
                .idStatusPagamento(1L)
                .nomeStatus(StatusPagamentoEnum.PENDENTE)
                .build();

        Cliente cliente = new Cliente(1L, "Cliente Teste", "test@test.com", "12345678901");

        when(gerenciarClienteUseCase.buscarCliente(1L)).thenReturn(cliente);
        when(gerenciarStatusPedido.buscarStatusPedidoPorNome(StatusPedidoEnum.RECEBIDO_NAO_PAGO.getStatus()))
                .thenReturn(statusPedidoNaoPago);
        when(gerenciarPedido.criarPedido(any())).thenReturn(pedidoTest);
        when(produtoRepository.buscarPorId(1L)).thenReturn(Optional.of(produto1));
        when(produtoRepository.buscarPorId(2L)).thenReturn(Optional.of(produto2));
        when(gerenciarStatusPagamento.buscarStatusPagamentoPorStatus(StatusPagamentoEnum.PENDENTE.getStatus()))
                .thenReturn(statusPagamentoPendente);
        when(pagamentoRepository.salvar(any())).thenAnswer(i -> i.getArgument(0));

        // Act
        service.orquestrarPedidoPagamento(request);

        // Assert - Total should be (2 * 10.0) + (3 * 20.0) = 20 + 60 = 80.0
        verify(pagamentoRepository).salvar(pagamentoCaptor.capture());
        Pagamento pagamentoSalvo = pagamentoCaptor.getValue();
        assertEquals(BigDecimal.valueOf(80.0).setScale(2), pagamentoSalvo.getValorTotal().setScale(2));
    }

    @Test
    @DisplayName("Deve validar pedido com quantidade zero de produtos")
    void deveValidarPedidoComQuantidadeZero() {
        // Arrange
        PedidoPagamentoRequest request = new PedidoPagamentoRequest();
        request.setIdCliente(1L);
        request.setMetodoPagamento("PIX");
        request.setProdutos(Arrays.asList(createItemProdutoRequest(1L, 0)));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.orquestrarPedidoPagamento(request));
        assertEquals("Quantidade de produtos deve ser maior que zero", exception.getMessage());
        verify(produtoRepository, never()).buscarPorId(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar orquestrar pedido com lista de produtos vazia")
    void deveLancarExcecaoListaProdutosVazia() {
        // Arrange 
        PedidoPagamentoRequest request = new PedidoPagamentoRequest();
        request.setIdCliente(1L);
        request.setMetodoPagamento("PIX");
        request.setProdutos(Collections.emptyList());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.orquestrarPedidoPagamento(request));
        assertEquals("Lista de produtos não pode estar vazia", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando request é null")
    void deveLancarExcecaoRequestNull() {
        // Act & Assert
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> service.orquestrarPedidoPagamento(null));
        assertNotNull(exception);
    }

    @Test
    @DisplayName("Deve lançar exceção quando método de pagamento é null")
    void deveLancarExcecaoMetodoPagamentoNull() {
        // Arrange
        PedidoPagamentoRequest request = new PedidoPagamentoRequest();
        request.setIdCliente(1L);
        request.setMetodoPagamento(null);
        request.setProdutos(Arrays.asList(createItemProdutoRequest(1L, 1)));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.orquestrarPedidoPagamento(request));
        assertEquals("Método de pagamento inválido", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando lista de produtos é null")
    void deveLancarExcecaoListaProdutosNull() {
        // Arrange
        PedidoPagamentoRequest request = new PedidoPagamentoRequest();
        request.setIdCliente(1L);
        request.setMetodoPagamento("PIX");
        request.setProdutos(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.orquestrarPedidoPagamento(request));
        assertEquals("Lista de produtos não pode estar vazia", exception.getMessage());
    }

    @Test
    @DisplayName("Deve verificar cálculo de desconto quando aplicável")
    void deveVerificarCalculoDesconto() {
        // Arrange
        PedidoPagamentoRequest request = new PedidoPagamentoRequest();
        request.setIdCliente(1L);
        request.setMetodoPagamento("PIX");
        request.setProdutos(Arrays.asList(
                createItemProdutoRequest(1L, 5), // 5 x 10.0 = 50.0
                createItemProdutoRequest(2L, 2) // 2 x 20.0 = 40.0
        ));

        Produto produto1 = Produto.builder()
                .id(1L)
                .preco(BigDecimal.valueOf(10.0))
                .build();

        Produto produto2 = Produto.builder()
                .id(2L)
                .preco(BigDecimal.valueOf(20.0))
                .build();

        StatusPedido statusPedidoNaoPago = StatusPedido.builder()
                .idStatusPedido(1L)
                .nomeStatus(StatusPedidoEnum.RECEBIDO_NAO_PAGO.getStatus())
                .build();

        StatusPagamento statusPagamentoPendente = StatusPagamento.builder()
                .idStatusPagamento(1L)
                .nomeStatus(StatusPagamentoEnum.PENDENTE)
                .build();

        Cliente cliente = new Cliente(1L, "Cliente Teste", "12345678900", "email@test.com");

        Pedido pedido = new Pedido(1L, 1L, LocalDateTime.now(), 1L, LocalDateTime.now(), 1);

        when(gerenciarClienteUseCase.buscarCliente(1L)).thenReturn(cliente);
        when(gerenciarStatusPedido.buscarStatusPedidoPorNome(StatusPedidoEnum.RECEBIDO_NAO_PAGO.getStatus()))
                .thenReturn(statusPedidoNaoPago);
        when(gerenciarPedido.criarPedido(any())).thenReturn(pedido);
        when(produtoRepository.buscarPorId(1L)).thenReturn(Optional.of(produto1));
        when(produtoRepository.buscarPorId(2L)).thenReturn(Optional.of(produto2));
        when(gerenciarStatusPagamento.buscarStatusPagamentoPorStatus(StatusPagamentoEnum.PENDENTE.getStatus()))
                .thenReturn(statusPagamentoPendente);
        when(pagamentoRepository.salvar(any())).thenAnswer(i -> i.getArgument(0));

        // Act
        service.orquestrarPedidoPagamento(request);

        // Assert - Total should be (5 * 10.0) + (2 * 20.0) = 50 + 40 = 90.0
        verify(pagamentoRepository).salvar(pagamentoCaptor.capture());
        Pagamento pagamentoSalvo = pagamentoCaptor.getValue();
        assertEquals(BigDecimal.valueOf(90.0).setScale(2), pagamentoSalvo.getValorTotal().setScale(2));
    }

    private PedidoPagamentoRequest.ItemProdutoRequest createItemProdutoRequest(Long idProduto, Integer quantidade) {
        return new PedidoPagamentoRequest.ItemProdutoRequest(idProduto, quantidade);
    }

    @Test
    @DisplayName("Deve validar transições de status de pagamento")
    void deveValidarTransicoesStatusPagamento() {
        // Arrange
        Long pedidoId = 1L;
        Pedido pedido = new Pedido(pedidoId, 1L, LocalDateTime.now(), 1L, LocalDateTime.now(), 1);

        StatusPedido statusEmAndamento = StatusPedido.builder()
                .idStatusPedido(2L)
                .nomeStatus(StatusPedidoEnum.EM_ANDAMENTO.getStatus())
                .build();

        StatusPagamento statusPagamentoFinalizado = StatusPagamento.builder()
                .idStatusPagamento(2L)
                .nomeStatus(StatusPagamentoEnum.FINALIZADO)
                .build();

        Pagamento pagamento = Pagamento.builder()
                .id(1L)
                .idPedido(pedidoId)
                .valorTotal(BigDecimal.valueOf(100.0))
                .metodoPagamento("PIX")
                .dataPagamento(LocalDateTime.now())
                .idStatusPagamento(1L)
                .build();

        when(gerenciarPedido.buscarPedido(pedidoId)).thenReturn(pedido);
        when(gerenciarPedido.atualizarPedido(any(), any())).thenReturn(pedido);
        when(pagamentoRepository.buscarPorId(pedidoId)).thenReturn(Optional.of(pagamento));
        when(gerenciarStatusPedido.buscarStatusPedidoPorNome(StatusPedidoEnum.EM_ANDAMENTO.getStatus()))
                .thenReturn(statusEmAndamento);
        when(gerenciarStatusPagamento.buscarStatusPagamentoPorStatus(StatusPagamentoEnum.FINALIZADO.getStatus()))
                .thenReturn(statusPagamentoFinalizado);
        when(pagamentoRepository.salvar(any())).thenReturn(pagamento);

        AtualizarPedidoRequest request = new AtualizarPedidoRequest();
        request.setId(pedidoId);
        request.setStatus(StatusPedidoEnum.EM_ANDAMENTO);

        // Act
        Pedido resultado = service.atualizarPedidoPagamento(request);

        // Assert
        assertNotNull(resultado);
        verify(pagamentoRepository).salvar(pagamentoCaptor.capture());
        assertEquals(statusPagamentoFinalizado.getIdStatusPagamento(), pagamentoCaptor.getValue().getIdStatusPagamento());
    }

    @Test
    @DisplayName("Deve atualizar pedido com status inválido")
    void deveLancarExcecaoAtualizarStatusInvalido() {
        // Arrange
        Long pedidoId = 1L;
        AtualizarPedidoRequest request = new AtualizarPedidoRequest();
        request.setId(pedidoId);
        request.setStatus(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.atualizarPedidoPagamento(request));
        assertEquals("Status do pedido não pode ser nulo", exception.getMessage());
    }
}
