package br.com.postech.techchallange.application.service;
import br.com.postech.techchallange.domain.model.Cliente;
import br.com.postech.techchallange.domain.port.out.ClienteRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GerenciarClienteServiceTest {

    private ClienteRepositoryPort clienteRepository;
    private GerenciarClienteService gerenciarClienteService;

    @BeforeEach
    void setUp() {
        clienteRepository = Mockito.mock(ClienteRepositoryPort.class);
        gerenciarClienteService = new GerenciarClienteService(clienteRepository);
    }

    @Test
    @DisplayName("Deve cadastrar um cliente com sucesso")
    void deveCadastrarClienteComSucesso() {
        // Arrange
        Cliente cliente = new Cliente(1L, "Joao", "joao@email.com", "12345678900");

        when(clienteRepository.salvar(cliente)).thenReturn(cliente);

        // Act
        Cliente resultado = gerenciarClienteService.cadastrarCliente(cliente);

        // Assert
        assertEquals(cliente, resultado);
        verify(clienteRepository).salvar(cliente);
    }

    @Test
    @DisplayName("Deve buscar um cliente existente pelo ID")
    void deveBuscarClienteExistente() {
        // Arrange
        Cliente cliente = new Cliente(1L, "Maria", "maria@email.com", "98765432100");

        when(clienteRepository.buscarPorId(1L)).thenReturn(Optional.of(cliente));

        // Act
        Cliente resultado = gerenciarClienteService.buscarCliente(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(cliente, resultado);
        verify(clienteRepository).buscarPorId(1L);
    }

    @Test
    @DisplayName("Deve retornar null quando o cliente não existir")
    void deveRetornarNullQuandoClienteNaoExiste() {

        when(clienteRepository.buscarPorId(99L)).thenReturn(Optional.empty());

        // Act
        Cliente resultado = gerenciarClienteService.buscarCliente(99L);

        // Assert
        assertNull(resultado);
        verify(clienteRepository).buscarPorId(99L);
    }

    @Test
    @DisplayName("Deve listar todos os clientes cadastrados")
    void deveListarTodosOsClientes() {
        // Arrange
        List<Cliente> clientes = Arrays.asList(
                new Cliente(1L, "Ana", "ana@email.com", "11122233344"),
                new Cliente(2L, "Carlos", "carlos@email.com", "55566677788")
        );

        when(clienteRepository.listarTodos()).thenReturn(clientes);

        // Act
        List<Cliente> resultado = gerenciarClienteService.listarClientes();

        // Assert
        assertEquals(2, resultado.size());
        assertEquals(clientes, resultado);
        verify(clienteRepository).listarTodos();
    }
}
