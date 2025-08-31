package br.com.postech.techchallange.application.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.postech.techchallange.domain.exception.BusinessException;
import br.com.postech.techchallange.domain.model.Produto;
import br.com.postech.techchallange.domain.port.in.ProdutoUseCase;
import br.com.postech.techchallange.domain.port.out.ProdutoRepositoryPort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProdutoService implements ProdutoUseCase {

	private final ProdutoRepositoryPort repository;

	@Override
	public Produto criarProduto(Produto produto) {
		validarProduto(produto);
		return this.repository.salvar(produto);
	}

	@Override
	public Produto atualizarProduto(Long id, Produto produtoAtualizado) {
		Produto produtoExistente = this.repository.buscarPorId(id)
				.orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

		validarProdutoParaAtualizacao(id, produtoAtualizado);

		produtoExistente.setNome(produtoAtualizado.getNome());
		produtoExistente.setDescricao(produtoAtualizado.getDescricao());
		produtoExistente.setPreco(produtoAtualizado.getPreco());
		produtoExistente.setIdImagemUrl(produtoAtualizado.getIdImagemUrl());
		produtoExistente.setIdCategoria(produtoAtualizado.getIdCategoria());

		return this.repository.salvar(produtoExistente);
	}

	@Override
	public Optional<Produto> buscarProdutoPorId(Long id) {
		return this.repository.buscarPorId(id);
	}

	@Override
	public List<Produto> listarTodosProdutos() {
		return this.repository.buscarTodos();
	}

	@Override
	public void deletarProduto(Long id) {
		this.repository.deletar(id);
	}

	@Override
	public List<Produto> buscarPorCategoria(Long idCategoria) {
		var prods = this.repository.buscarPorCategoria(idCategoria);
		if (Objects.isNull(prods) || prods.isEmpty()) {
			throw new BusinessException(String.format("Nenhum produto para categoria %s foi encontrado", idCategoria));
		}
		
		return prods;
	}

	private void validarProduto(Produto produto) {
		if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
			throw new BusinessException("Nome do produto não pode ser vazio");
		}

		if (produto.getIdCategoria() == null) {
			throw new BusinessException("Categoria do produto é obrigatória");
		}

		if (produto.getIdCategoria() <= 0) {
			throw new BusinessException("Categoria do produto inválida");
		}
	}

	private void validarProdutoParaAtualizacao(Long id, Produto produto) {
		validarProduto(produto);

		if (produto.getId() != null && !produto.getId().equals(id)) {
			throw new BusinessException("ID do produto não pode ser alterado");
		}
	}
}
