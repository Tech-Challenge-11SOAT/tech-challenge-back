package br.com.postech.techchallange.application.service;

import java.util.Optional;

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

import br.com.postech.techchallange.domain.port.out.RedisPort;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class ProcessarPagamentoServiceTest {

    @Mock
    private RedisPort<String, String> redisQrcodeService;

    private ProcessarPagamentoService service;

    @BeforeEach
    void setUp() {
        service = new ProcessarPagamentoService(redisQrcodeService);
    }

    @Test
    @DisplayName("Deve salvar QRCode com sucesso")
    void deveSalvarQRCodeComSucesso() {
        // Arrange
        String idPedido = "123";
        String qrCode = "dados-qrcode-123";
        doNothing().when(redisQrcodeService).salvar(idPedido, qrCode);

        // Act
        assertDoesNotThrow(() -> service.salvarQRCode(idPedido, qrCode));

        // Assert
        verify(redisQrcodeService).salvar(idPedido, qrCode);
    }

    @Test
    @DisplayName("Deve propagar exceção ao tentar salvar QRCode quando redis falhar")
    void devePropararExcecaoAoSalvarQRCode() {
        // Arrange
        String idPedido = "123";
        String qrCode = "dados-qrcode-123";
        doThrow(new RuntimeException("Erro ao salvar no Redis"))
            .when(redisQrcodeService).salvar(idPedido, qrCode);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> service.salvarQRCode(idPedido, qrCode));
        
        assertEquals("Erro ao salvar no Redis", exception.getMessage());
        verify(redisQrcodeService).salvar(idPedido, qrCode);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar salvar QRCode com ID nulo")
    void deveLancarExcecaoAoSalvarComIdNulo() {
        // Arrange
        String idPedido = null;
        String qrCode = "dados-qrcode-123";
        doThrow(new IllegalArgumentException("ID do pedido não pode ser nulo"))
            .when(redisQrcodeService).salvar(idPedido, qrCode);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> service.salvarQRCode(idPedido, qrCode));
        
        assertEquals("ID do pedido não pode ser nulo", exception.getMessage());
        verify(redisQrcodeService).salvar(idPedido, qrCode);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar salvar QRCode nulo")
    void deveLancarExcecaoAoSalvarQRCodeNulo() {
        // Arrange
        String idPedido = "123";
        String qrCode = null;
        doThrow(new IllegalArgumentException("QRCode não pode ser nulo"))
            .when(redisQrcodeService).salvar(idPedido, qrCode);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> service.salvarQRCode(idPedido, qrCode));
        
        assertEquals("QRCode não pode ser nulo", exception.getMessage());
        verify(redisQrcodeService).salvar(idPedido, qrCode);
    }

    @Test
    @DisplayName("Deve obter QRCode existente com sucesso")
    void deveObterQRCodeExistenteComSucesso() {
        // Arrange
        String idPedido = "123";
        String qrCode = "dados-qrcode-123";
        when(redisQrcodeService.obter(idPedido)).thenReturn(Optional.of(qrCode));

        // Act
        String resultado = service.obterQRCode(idPedido);

        // Assert
        assertNotNull(resultado);
        assertEquals(qrCode, resultado);
        verify(redisQrcodeService).obter(idPedido);
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException ao tentar obter QRCode inexistente")
    void deveLancarExcecaoAoObterQRCodeInexistente() {
        // Arrange
        String idPedido = "123";
        when(redisQrcodeService.obter(idPedido)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
            () -> service.obterQRCode(idPedido));
        
        assertEquals("QRCode não encontrado", exception.getMessage());
        verify(redisQrcodeService).obter(idPedido);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar obter QRCode com ID nulo")
    void deveLancarExcecaoAoObterComIdNulo() {
        // Arrange
        String idPedido = null;
        when(redisQrcodeService.obter(idPedido)).thenThrow(new IllegalArgumentException("ID do pedido não pode ser nulo"));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> service.obterQRCode(idPedido));
        
        assertEquals("ID do pedido não pode ser nulo", exception.getMessage());
        verify(redisQrcodeService).obter(idPedido);
    }
}
