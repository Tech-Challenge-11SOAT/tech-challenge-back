package br.com.postech.techchallange.adapter.out.persistence.adapter;
import br.com.postech.techchallange.adapter.out.persistence.entity.ClienteEntity;
import br.com.postech.techchallange.adapter.out.persistence.mapper.ClienteMapper;
import br.com.postech.techchallange.adapter.out.persistence.repository.ClienteJpaRepository;
import br.com.postech.techchallange.domain.model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ClienteRepositoryAdapterTest {

    private ClienteJpaRepository jpaRepository;
    private ClienteRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        jpaRepository = mock(ClienteJpaRepository.class);
        adapter = new ClienteRepositoryAdapter(jpaRepository);
    }

    @Test
    @DisplayName("Deve salvar as informações do cliente no banco")
    void deveSalvarClienteComSucesso() {
        // Arrange
        Cliente cliente = new Cliente(1L, "João", "joao@email.com", "12345678900");
        ClienteEntity entity = ClienteMapper.toEntity(cliente);

        when(jpaRepository.save(any(ClienteEntity.class))).thenReturn(entity);

        // Act
        Cliente resultado = adapter.salvar(cliente);

        // Assert
        assertNotNull(resultado);
        assertEquals(cliente.getId(), resultado.getId());
        verify(jpaRepository).save(any(ClienteEntity.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar cliente quando ocorrer erro no repositório")
    void deveFalharAoSalvarCliente() {
        // Arrange
        Cliente cliente = new Cliente(null, "Erro", "erro@email.com", "00000000000");
        when(jpaRepository.save(any(ClienteEntity.class)))
                .thenThrow(new RuntimeException("Erro ao salvar"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> adapter.salvar(cliente));
        verify(jpaRepository).save(any(ClienteEntity.class));
    }


    @Test
    @DisplayName("Deve buscar as informações do cliente através do Id no banco")
    void deveBuscarClientePorIdComSucesso() {
        // Arrange
        Cliente cliente = new Cliente(2L, "Maria", "maria@email.com", "98765432100");
        ClienteEntity entity = ClienteMapper.toEntity(cliente);

        when(jpaRepository.findById(2L)).thenReturn(Optional.of(entity));

        // Act
        Optional<Cliente> resultado = adapter.buscarPorId(2L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("Maria", resultado.get().getNomeCliente());
        verify(jpaRepository).findById(2L);
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar cliente por ID inexistente")
    void deveFalharAoBuscarClientePorIdInexistente() {
        // Arrange
        when(jpaRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Cliente> resultado = adapter.buscarPorId(99L);

        // Assert
        assertFalse(resultado.isPresent());
        verify(jpaRepository).findById(99L);
    }


    @Test
    @DisplayName("Deve listar as informações dos clientes do banco")
    void deveListarTodosOsClientes() {
        // Arrange
        ClienteEntity cliente1 = ClienteMapper.toEntity(new Cliente(1L, "A", "a@email.com", "111"));
        ClienteEntity cliente2 = ClienteMapper.toEntity(new Cliente(2L, "B", "b@email.com", "222"));

        when(jpaRepository.findAll()).thenReturn(Arrays.asList(cliente1, cliente2));

        // Act
        List<Cliente> resultado = adapter.listarTodos();

        // Assert
        assertEquals(2, resultado.size());
        verify(jpaRepository).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver clientes cadastrados")
    void deveRetornarListaVaziaSeNaoHouverClientes() {
        // Arrange
        when(jpaRepository.findAll()).thenReturn(List.of());

        // Act
        List<Cliente> resultado = adapter.listarTodos();

        // Assert
        assertTrue(resultado.isEmpty());
        verify(jpaRepository).findAll();
    }

}