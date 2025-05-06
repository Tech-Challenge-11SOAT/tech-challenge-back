package br.com.postech.techchallange.domain.port.out;

import java.util.List;
import java.util.Optional;

import br.com.postech.techchallange.domain.model.Produto;

public interface ProdutoRepositoryPort {

	Produto salvar(Produto produto);

	Optional<Produto> buscarPorId(Long id);

	List<Produto> buscarTodos();

	void deletar(Long id);

	List<Produto> buscarPorCategoria(Long idCategoria);
}
