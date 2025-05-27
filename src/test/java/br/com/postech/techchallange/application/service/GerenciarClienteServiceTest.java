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

    private ClienteRepositoryPort repository;
    private GerenciarClienteService service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(ClienteRepositoryPort.class);
        service = new GerenciarClienteService(repository);
    }


    // Testes para salvar informações ---------------------------------------------------------------


    @Test
    @DisplayName("Deve cadastrar um cliente com sucesso")
    void deveCadastrarClienteComSucesso() {
        // Arrange
        Cliente cliente = new Cliente(1L, "Joao", "joao@email.com", "12345678900");

        when(repository.salvar(cliente)).thenReturn(cliente);

        // Act
        Cliente resultado = service.cadastrarCliente(cliente);

        // Assert
        assertEquals(cliente, resultado);
        verify(repository).salvar(cliente);
    }


    // Testes para buscar cliente por ID ---------------------------------------------------------------


    @Test
    @DisplayName("Deve buscar um cliente existente pelo ID")
    void deveBuscarClienteExistente() {
        // Arrange
        Cliente cliente = new Cliente(1L, "Maria", "maria@email.com", "98765432100");

        when(repository.buscarPorId(1L)).thenReturn(Optional.of(cliente));

        // Act
        Cliente resultado = service.buscarCliente(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(cliente, resultado);
        verify(repository).buscarPorId(1L);
    }

    @Test
    @DisplayName("Deve retornar null quando o cliente não existir")
    void deveRetornarNullQuandoClienteNaoExiste() {

        when(repository.buscarPorId(99L)).thenReturn(Optional.empty());

        // Act
        Cliente resultado = service.buscarCliente(99L);

        // Assert
        assertNull(resultado);
        verify(repository).buscarPorId(99L);
    }


    // Testes para listar os clientes ---------------------------------------------------------------


    @Test
    @DisplayName("Deve listar todos os clientes cadastrados")
    void deveListarTodosOsClientes() {
        // Arrange
        List<Cliente> clientes = Arrays.asList(
                new Cliente(1L, "Ana", "ana@email.com", "11122233344"),
                new Cliente(2L, "Carlos", "carlos@email.com", "55566677788")
        );

        when(repository.listarTodos()).thenReturn(clientes);

        // Act
        List<Cliente> resultado = service.listarClientes();

        // Assert
        assertEquals(2, resultado.size());
        assertEquals(clientes, resultado);
        verify(repository).listarTodos();
    }
}
