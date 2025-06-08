package br.com.postech.techchallange.adapter.out.persistence.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoriaEntityTest {

    @Test
    void testDefaultConstructor() {
        CategoriaEntity categoria = new CategoriaEntity();
        assertNotNull(categoria);
        assertNull(categoria.getId_categoria());
        assertNull(categoria.getNome());
    }

    @Test
    void testSettersAndGetters() {
        CategoriaEntity categoria = new CategoriaEntity();

        Long id = 1L;
        String nome = "Categoria Teste";

        categoria.setId_categoria(id);
        categoria.setNome(nome);

        assertEquals(id, categoria.getId_categoria());
        assertEquals(nome, categoria.getNome());
    }

    @Test
    void testEntityAnnotations() {
        // Test if the class has the required JPA annotations
        assertTrue(CategoriaEntity.class.isAnnotationPresent(jakarta.persistence.Entity.class));
        assertTrue(CategoriaEntity.class.isAnnotationPresent(jakarta.persistence.Table.class));

        // Test table name
        jakarta.persistence.Table tableAnnotation = CategoriaEntity.class.getAnnotation(jakarta.persistence.Table.class);
        assertEquals("categoria", tableAnnotation.name());

        // Test ID field annotations
        try {
            java.lang.reflect.Field idField = CategoriaEntity.class.getDeclaredField("id_categoria");
            assertTrue(idField.isAnnotationPresent(jakarta.persistence.Id.class));
            assertTrue(idField.isAnnotationPresent(jakarta.persistence.GeneratedValue.class));

            jakarta.persistence.GeneratedValue generatedValue = idField.getAnnotation(jakarta.persistence.GeneratedValue.class);
            assertEquals(jakarta.persistence.GenerationType.IDENTITY, generatedValue.strategy());
        } catch (NoSuchFieldException e) {
            fail("Field id_categoria not found");
        }
    }
}
