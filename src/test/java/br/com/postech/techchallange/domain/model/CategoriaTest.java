package br.com.postech.techchallange.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class CategoriaTest {

    @Test
    void testParameterizedConstructor() {
        Long id = 1L;
        String nome = "Categoria Teste";

        Categoria categoria = new Categoria(id, nome);

        assertEquals(id, categoria.getId());
        assertEquals(nome, categoria.getNome());
    }

    @Test
    void testGetters() {
        Long id = 1L;
        String nome = "Categoria Teste";

        Categoria categoria = new Categoria(id, nome);

        assertEquals(id, categoria.getId());
        assertEquals(nome, categoria.getNome());
    }
}
