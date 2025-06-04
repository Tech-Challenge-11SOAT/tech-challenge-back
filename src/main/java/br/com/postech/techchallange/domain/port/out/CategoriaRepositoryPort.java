package br.com.postech.techchallange.domain.port.out;

import br.com.postech.techchallange.domain.model.Categoria;
import java.util.Optional;

public interface CategoriaRepositoryPort {

    Optional<Categoria> buscarPorId(Long id);
}
