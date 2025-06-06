package br.com.postech.techchallange.application.service;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.postech.techchallange.domain.model.AdminLogAcao;
import br.com.postech.techchallange.domain.port.out.AdminLogAcaoRepositoryPort;

@ExtendWith(MockitoExtension.class)
class LogAdminActionServiceTest {

    @Mock
    private AdminLogAcaoRepositoryPort adminLogRepository;

    @Captor
    private ArgumentCaptor<AdminLogAcao> logCaptor;

    private LogAdminActionService service;

    @BeforeEach
    void setUp() {
        service = new LogAdminActionService(adminLogRepository);
    }

    @Test
    @DisplayName("Deve registrar log de ação com sucesso")
    void deveRegistrarLogComSucesso() {
        // Arrange
        Long idAdmin = 1L;
        String acao = "CRIAR";
        String recursoAfetado = "PRODUTO";
        Long idRecursoAfetado = 10L;
        LocalDateTime antes = LocalDateTime.now();

        // Act
        service.registrar(idAdmin, acao, recursoAfetado, idRecursoAfetado);

        // Assert
        verify(adminLogRepository).registrarLog(logCaptor.capture());
        AdminLogAcao logRegistrado = logCaptor.getValue();
        
        assertNotNull(logRegistrado);
        assertEquals(idAdmin, logRegistrado.getIdAdmin());
        assertEquals(acao, logRegistrado.getAcao());
        assertEquals(recursoAfetado, logRegistrado.getRecursoAfetado());
        assertEquals(idRecursoAfetado, logRegistrado.getIdRecurso());
        assertNotNull(logRegistrado.getDataAcao());
        assertTrue(logRegistrado.getDataAcao().isAfter(antes) || 
                  logRegistrado.getDataAcao().isEqual(antes));
    }

    @Test
    @DisplayName("Deve registrar log mesmo com ID de recurso nulo")
    void deveRegistrarLogComIdRecursoNulo() {
        // Arrange
        Long idAdmin = 1L;
        String acao = "LISTAR";
        String recursoAfetado = "PRODUTOS";
        Long idRecursoAfetado = null;

        // Act
        service.registrar(idAdmin, acao, recursoAfetado, idRecursoAfetado);

        // Assert
        verify(adminLogRepository).registrarLog(logCaptor.capture());
        AdminLogAcao logRegistrado = logCaptor.getValue();
        
        assertNotNull(logRegistrado);
        assertEquals(idAdmin, logRegistrado.getIdAdmin());
        assertEquals(acao, logRegistrado.getAcao());
        assertEquals(recursoAfetado, logRegistrado.getRecursoAfetado());
        assertNull(logRegistrado.getIdRecurso());
    }

    @Test
    @DisplayName("Deve propagar exceção quando repositório falhar")
    void devePropararExcecaoDoRepositorio() {
        // Arrange
        Long idAdmin = 1L;
        String acao = "CRIAR";
        String recursoAfetado = "PRODUTO";
        Long idRecursoAfetado = 10L;
        
        doThrow(new RuntimeException("Erro ao salvar log"))
            .when(adminLogRepository).registrarLog(any(AdminLogAcao.class));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> service.registrar(idAdmin, acao, recursoAfetado, idRecursoAfetado));
        
        assertEquals("Erro ao salvar log", exception.getMessage());
        verify(adminLogRepository).registrarLog(any(AdminLogAcao.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID do admin for nulo")
    void deveLancarExcecaoComIdAdminNulo() {
        // Arrange
        Long idAdmin = null;
        String acao = "CRIAR";
        String recursoAfetado = "PRODUTO";
        Long idRecursoAfetado = 10L;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> service.registrar(idAdmin, acao, recursoAfetado, idRecursoAfetado));
        
        assertEquals("ID do admin não pode ser nulo", exception.getMessage());
        verify(adminLogRepository, never()).registrarLog(any(AdminLogAcao.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando ação for nula")
    void deveLancarExcecaoComAcaoNula() {
        // Arrange
        Long idAdmin = 1L;
        String acao = null;
        String recursoAfetado = "PRODUTO";
        Long idRecursoAfetado = 10L;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> service.registrar(idAdmin, acao, recursoAfetado, idRecursoAfetado));
        
        assertEquals("Ação não pode ser nula", exception.getMessage());
        verify(adminLogRepository, never()).registrarLog(any(AdminLogAcao.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando recurso afetado for nulo")
    void deveLancarExcecaoComRecursoAfetadoNulo() {
        // Arrange
        Long idAdmin = 1L;
        String acao = "CRIAR";
        String recursoAfetado = null;
        Long idRecursoAfetado = 10L;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> service.registrar(idAdmin, acao, recursoAfetado, idRecursoAfetado));
        
        assertEquals("Recurso afetado não pode ser nulo", exception.getMessage());
        verify(adminLogRepository, never()).registrarLog(any(AdminLogAcao.class));
    }
}
