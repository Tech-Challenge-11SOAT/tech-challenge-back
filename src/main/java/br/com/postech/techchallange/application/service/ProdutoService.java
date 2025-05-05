package br.com.postech.techchallange.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Produto atualizarProduto(Long id, Produto produto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Produto> buscarProdutoPorId(Long id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public List<Produto> listarTodosProdutos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deletarProduto(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Produto> buscarPorCategoria(Long idCategoria) {
		// TODO Auto-generated method stub
		return null;
	}

}
