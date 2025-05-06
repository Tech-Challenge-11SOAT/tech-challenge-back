package br.com.postech.techchallange.domain.port.in;

import java.util.List;
import java.util.Optional;

import br.com.postech.techchallange.domain.model.Produto;

public interface ProdutoUseCase {
	Produto criarProduto(Produto produto);
	Produto atualizarProduto(Long id, Produto produto);
	Optional<Produto> buscarProdutoPorId(Long id);
	List<Produto> listarTodosProdutos();
	void deletarProduto(Long id);
	List<Produto> buscarPorCategoria(Long idCategoria);
}
