package br.com.postech.techchallange.adapter.out.persistence.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class ClienteEntityTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    void testDefaultConstructor() {
        ClienteEntity cliente = new ClienteEntity();
        assertNotNull(cliente);
        assertNull(cliente.getIdCliente());
        assertNull(cliente.getNomeCliente());
        assertNull(cliente.getEmailCliente());
        assertNull(cliente.getCpfCliente());
    }

    @Test
    void testParameterizedConstructor() {
        Long idCliente = 1L;
        String nomeCliente = "João Silva";
        String emailCliente = "joao.silva@email.com";
        String cpfCliente = "12345678900";

        ClienteEntity cliente = new ClienteEntity(idCliente, nomeCliente, emailCliente, cpfCliente);

        assertEquals(idCliente, cliente.getIdCliente());
        assertEquals(nomeCliente, cliente.getNomeCliente());
        assertEquals(emailCliente, cliente.getEmailCliente());
        assertEquals(cpfCliente, cliente.getCpfCliente());
    }

    @Test
    void testSettersAndGetters() {
        ClienteEntity cliente = new ClienteEntity();

        Long idCliente = 1L;
        String nomeCliente = "João Silva";
        String emailCliente = "joao.silva@email.com";
        String cpfCliente = "12345678900";

        cliente.setIdCliente(idCliente);
        cliente.setNomeCliente(nomeCliente);
        cliente.setEmailCliente(emailCliente);
        cliente.setCpfCliente(cpfCliente);

        assertEquals(idCliente, cliente.getIdCliente());
        assertEquals(nomeCliente, cliente.getNomeCliente());
        assertEquals(emailCliente, cliente.getEmailCliente());
        assertEquals(cpfCliente, cliente.getCpfCliente());
    }

    @Test
    void testEntityAnnotations() {
        // Test if the class has the required JPA annotations
        assertTrue(ClienteEntity.class.isAnnotationPresent(jakarta.persistence.Entity.class));
        assertTrue(ClienteEntity.class.isAnnotationPresent(jakarta.persistence.Table.class));

        // Test table name
        jakarta.persistence.Table tableAnnotation = ClienteEntity.class.getAnnotation(jakarta.persistence.Table.class);
        assertEquals("cliente", tableAnnotation.name());

        // Test ID field annotations
        try {
            java.lang.reflect.Field idField = ClienteEntity.class.getDeclaredField("idCliente");
            assertTrue(idField.isAnnotationPresent(jakarta.persistence.Id.class));
            assertTrue(idField.isAnnotationPresent(jakarta.persistence.GeneratedValue.class));
            assertTrue(idField.isAnnotationPresent(jakarta.persistence.Column.class));

            jakarta.persistence.GeneratedValue generatedValue = idField.getAnnotation(jakarta.persistence.GeneratedValue.class);
            assertEquals(jakarta.persistence.GenerationType.IDENTITY, generatedValue.strategy());

            jakarta.persistence.Column column = idField.getAnnotation(jakarta.persistence.Column.class);
            assertEquals("id_cliente", column.name());
        } catch (NoSuchFieldException e) {
            fail("Field idCliente not found");
        }
    }

    @Test
    void testValidationConstraints() {
        ClienteEntity cliente = new ClienteEntity();

        // Test empty values
        var violations = validator.validate(cliente);
        assertEquals(3, violations.size()); // nome, email, and cpf are required

        // Test invalid email
        cliente.setNomeCliente("João Silva");
        cliente.setEmailCliente("invalid-email");
        cliente.setCpfCliente("12345678900");
        violations = validator.validate(cliente);
        assertEquals(1, violations.size()); // only email validation should fail

        // Test invalid CPF format
        cliente.setEmailCliente("joao.silva@email.com");
        cliente.setCpfCliente("123.456.789-00");
        violations = validator.validate(cliente);
        assertEquals(1, violations.size()); // only CPF validation should fail

        // Test valid data
        cliente.setCpfCliente("12345678900");
        violations = validator.validate(cliente);
        assertTrue(violations.isEmpty());
    }
}
