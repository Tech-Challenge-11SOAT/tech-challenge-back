package br.com.postech.techchallange.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

class ClienteTest {

    @Test
    void testDefaultConstructor() {
        Cliente cliente = new Cliente();
        assertNotNull(cliente);
        assertNull(cliente.getId());
        assertNull(cliente.getNomeCliente());
        assertNull(cliente.getEmailCliente());
        assertNull(cliente.getCpfCliente());
    }

    @Test
    void testParameterizedConstructor() {
        Long id = 1L;
        String nomeCliente = "João Silva";
        String emailCliente = "joao.silva@email.com";
        String cpfCliente = "123.456.789-00";

        Cliente cliente = new Cliente(id, nomeCliente, emailCliente, cpfCliente);

        assertEquals(id, cliente.getId());
        assertEquals(nomeCliente, cliente.getNomeCliente());
        assertEquals(emailCliente, cliente.getEmailCliente());
        assertEquals(cpfCliente, cliente.getCpfCliente());
    }

    @Test
    void testSettersAndGetters() {
        Cliente cliente = new Cliente();

        Long id = 1L;
        String nomeCliente = "João Silva";
        String emailCliente = "joao.silva@email.com";
        String cpfCliente = "123.456.789-00";

        cliente.setId(id);
        cliente.setNomeCliente(nomeCliente);
        cliente.setEmailCliente(emailCliente);
        cliente.setCpfCliente(cpfCliente);

        assertEquals(id, cliente.getId());
        assertEquals(nomeCliente, cliente.getNomeCliente());
        assertEquals(emailCliente, cliente.getEmailCliente());
        assertEquals(cpfCliente, cliente.getCpfCliente());
    }
}
