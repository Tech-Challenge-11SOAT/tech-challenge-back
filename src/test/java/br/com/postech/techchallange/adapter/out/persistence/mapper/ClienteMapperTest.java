package br.com.postech.techchallange.adapter.out.persistence.mapper;

import br.com.postech.techchallange.adapter.out.persistence.entity.ClienteEntity;
import br.com.postech.techchallange.domain.model.Cliente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ClienteMapperTest {

    @Test
    @DisplayName("Deve converter ClienteEntity para ClienteDomain")
    void deveConverterClienteEntityParaClienteDomain() {
        // Arrange
        ClienteEntity entity = new ClienteEntity();
        entity.setIdCliente(1L);
        entity.setNomeCliente("Joao Silva");
        entity.setEmailCliente("joao@example.com");
        entity.setCpfCliente("12345678900");

        // Act
        Cliente cliente = ClienteMapper.toDomain(entity);

        // Assert
        assertThat(cliente).isNotNull();
        assertThat(cliente.getId()).isEqualTo(1L);
        assertThat(cliente.getNomeCliente()).isEqualTo("Joao Silva");
        assertThat(cliente.getEmailCliente()).isEqualTo("joao@example.com");
        assertThat(cliente.getCpfCliente()).isEqualTo("12345678900");
    }

    @Test
    @DisplayName("Deve converter ClienteDomain para ClienteEntity")
    void deveConverterClienteDomainParaClienteEntity() {
        // Arrange
        Cliente cliente = new Cliente(2L, "Maria Souza", "maria@example.com", "98765432100");

        // Act
        ClienteEntity entity = ClienteMapper.toEntity(cliente);

        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity.getIdCliente()).isEqualTo(2L);
        assertThat(entity.getNomeCliente()).isEqualTo("Maria Souza");
        assertThat(entity.getEmailCliente()).isEqualTo("maria@example.com");
        assertThat(entity.getCpfCliente()).isEqualTo("98765432100");
    }
}
