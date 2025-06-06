package br.com.postech.techchallange.application.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Captor;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.postech.techchallange.domain.exception.BusinessException;
import br.com.postech.techchallange.domain.model.AdminUser;
import br.com.postech.techchallange.domain.port.out.AdminUserRepositoryPort;

@ExtendWith(MockitoExtension.class)
class InativarAdminServiceTest {

    @Mock
    private AdminUserRepositoryPort adminUserRepository;

    @Captor
    private ArgumentCaptor<AdminUser> adminCaptor;

    private InativarAdminService service;

    @BeforeEach
    void setUp() {
        service = new InativarAdminService(adminUserRepository);
    }

    @Test
    @DisplayName("Deve alternar status de admin ativo para inativo")
    void deveInativarAdminAtivo() {
        // Arrange
        Long idAdmin = 1L;
        AdminUser adminAtivo = AdminUser.builder()
                .id(idAdmin)
                .nome("Admin Teste")
                .email("admin@test.com")
                .ativo(true)
                .build();

        when(adminUserRepository.buscarPorId(idAdmin)).thenReturn(Optional.of(adminAtivo));
        when(adminUserRepository.salvar(any(AdminUser.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        service.toggle(idAdmin);

        // Assert
        verify(adminUserRepository).salvar(adminCaptor.capture());
        AdminUser adminSalvo = adminCaptor.getValue();

        assertFalse(adminSalvo.getAtivo());
        assertEquals(idAdmin, adminSalvo.getId());
        assertEquals("Admin Teste", adminSalvo.getNome());
        assertEquals("admin@test.com", adminSalvo.getEmail());
    }

    @Test
    @DisplayName("Deve alternar status de admin inativo para ativo")
    void deveAtivarAdminInativo() {
        // Arrange
        Long idAdmin = 1L;
        AdminUser adminInativo = AdminUser.builder()
                .id(idAdmin)
                .nome("Admin Teste")
                .email("admin@test.com")
                .ativo(false)
                .build();

        when(adminUserRepository.buscarPorId(idAdmin)).thenReturn(Optional.of(adminInativo));
        when(adminUserRepository.salvar(any(AdminUser.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        service.toggle(idAdmin);

        // Assert
        verify(adminUserRepository).salvar(adminCaptor.capture());
        AdminUser adminSalvo = adminCaptor.getValue();

        assertTrue(adminSalvo.getAtivo());
        assertEquals(idAdmin, adminSalvo.getId());
        assertEquals("Admin Teste", adminSalvo.getNome());
        assertEquals("admin@test.com", adminSalvo.getEmail());
    }

    @Test
    @DisplayName("Deve lançar exceção quando admin não for encontrado")
    void deveLancarExcecaoQuandoAdminNaoExistir() {
        // Arrange
        Long idAdmin = 1L;
        when(adminUserRepository.buscarPorId(idAdmin)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> service.toggle(idAdmin));

        assertEquals("Administrador não encontrado", exception.getMessage());
        verify(adminUserRepository).buscarPorId(idAdmin);
        verify(adminUserRepository, never()).salvar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID for nulo")
    void deveLancarExcecaoQuandoIdForNulo() {
        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> service.toggle(null));

        assertEquals("ID do administrador não pode ser nulo", exception.getMessage());
        verify(adminUserRepository, never()).buscarPorId(any());
        verify(adminUserRepository, never()).salvar(any());
    }
}
