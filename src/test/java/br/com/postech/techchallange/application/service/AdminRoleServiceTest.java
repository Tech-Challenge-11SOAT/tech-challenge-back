package br.com.postech.techchallange.application.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.postech.techchallange.domain.model.AdminRole;
import br.com.postech.techchallange.domain.port.out.AdminRoleRepositoryPort;

@ExtendWith(MockitoExtension.class)
class AdminRoleServiceTest {

    @Mock
    private AdminRoleRepositoryPort roleRepository;

    private AdminRoleService service;

    @BeforeEach
    void setUp() {
        service = new AdminRoleService(roleRepository);
    }

    // Testes para cadastrar role
    @Test
    @DisplayName("Deve cadastrar role com sucesso")
    void deveCadastrarRoleComSucesso() {
        // Arrange
        AdminRole role = AdminRole.builder()
                .id(1L)
                .nome("ADMIN")
                .build();
        when(roleRepository.salvar(role)).thenReturn(role);

        // Act
        AdminRole resultado = service.cadastrar(role);

        // Assert
        assertNotNull(resultado);
        assertEquals(role.getId(), resultado.getId());
        assertEquals(role.getNome(), resultado.getNome());
        verify(roleRepository).salvar(role);
    }

    @Test
    @DisplayName("Deve cadastrar role mesmo sem ID definido")
    void deveCadastrarRoleSemId() {
        // Arrange
        AdminRole roleEntrada = AdminRole.builder()
                .nome("ADMIN")
                .build();
        
        AdminRole roleSaida = AdminRole.builder()
                .id(1L)
                .nome("ADMIN")
                .build();
                
        when(roleRepository.salvar(roleEntrada)).thenReturn(roleSaida);

        // Act
        AdminRole resultado = service.cadastrar(roleEntrada);

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals(roleEntrada.getNome(), resultado.getNome());
        verify(roleRepository).salvar(roleEntrada);
    }

    // Testes para atribuir role
    @Test
    @DisplayName("Deve atribuir role a um admin com sucesso")
    void deveAtribuirRoleComSucesso() {
        // Arrange
        Long idAdmin = 1L;
        Long idRole = 1L;
        doNothing().when(roleRepository).atribuirRole(idAdmin, idRole);

        // Act & Assert
        assertDoesNotThrow(() -> service.atribuirRole(idAdmin, idRole));
        verify(roleRepository).atribuirRole(idAdmin, idRole);
    }

    @Test
    @DisplayName("Deve propagar exceção ao tentar atribuir role com erro no repositório")
    void devePropararExcecaoAoAtribuirRole() {
        // Arrange
        Long idAdmin = 1L;
        Long idRole = 1L;
        doThrow(new RuntimeException("Erro ao atribuir role"))
            .when(roleRepository).atribuirRole(idAdmin, idRole);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> service.atribuirRole(idAdmin, idRole));
        
        assertEquals("Erro ao atribuir role", exception.getMessage());
        verify(roleRepository).atribuirRole(idAdmin, idRole);
    }

    @Test
    @DisplayName("Deve propagar exceção ao tentar atribuir role com ID de admin nulo")
    void devePropararExcecaoComIdAdminNulo() {
        // Arrange
        Long idAdmin = null;
        Long idRole = 1L;
        doThrow(new IllegalArgumentException("ID do admin não pode ser nulo"))
            .when(roleRepository).atribuirRole(idAdmin, idRole);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> service.atribuirRole(idAdmin, idRole));
        
        assertEquals("ID do admin não pode ser nulo", exception.getMessage());
        verify(roleRepository).atribuirRole(idAdmin, idRole);
    }

    @Test
    @DisplayName("Deve propagar exceção ao tentar atribuir role com ID de role nulo")
    void devePropararExcecaoComIdRoleNulo() {
        // Arrange
        Long idAdmin = 1L;
        Long idRole = null;
        doThrow(new IllegalArgumentException("ID da role não pode ser nulo"))
            .when(roleRepository).atribuirRole(idAdmin, idRole);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> service.atribuirRole(idAdmin, idRole));
        
        assertEquals("ID da role não pode ser nulo", exception.getMessage());
        verify(roleRepository).atribuirRole(idAdmin, idRole);
    }
}
