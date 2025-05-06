package br.com.postech.techchallange.adapter.out.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import br.com.postech.techchallange.adapter.out.persistence.entity.ProdutoEntity;
import br.com.postech.techchallange.adapter.out.persistence.mapper.ProdutoMapper;
import br.com.postech.techchallange.adapter.out.persistence.repository.ProdutoJpaRepository;
import br.com.postech.techchallange.domain.model.Produto;
import br.com.postech.techchallange.domain.port.out.ProdutoRepositoryPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProdutoRepositoryAdapter implements ProdutoRepositoryPort {
	
	private final ProdutoJpaRepository produtoRepository;

	@Override
	public Produto salvar(Produto produto) {
		ProdutoEntity entity = ProdutoMapper.toEntity(produto);
		ProdutoEntity savedEntity = produtoRepository.save(entity);
		return ProdutoMapper.toDomain(savedEntity);
	}

	@Override
	public Optional<Produto> buscarPorId(Long id) {
		return Optional.ofNullable(this.produtoRepository.findById(id)
				.map(ProdutoMapper::toDomain)
				.orElseThrow(() -> new EntityNotFoundException("Produto não encontrado.")));
	}

	@Override
	public List<Produto> buscarTodos() {
		return this.produtoRepository.findAll().stream()
				.map(ProdutoMapper::toDomain)
				.toList();
	}

	@Override
	public void deletar(Long id) {
		var pedido = this.buscarPorId(id)
				.map(ProdutoMapper::toEntity)
				.get();

		this.produtoRepository.delete(pedido);
	}

	@Override
	public List<Produto> buscarPorCategoria(Long idCategoria) {
		return this.produtoRepository.findByIdCategoria(idCategoria).stream()
				.map(ProdutoMapper::toDomain)
				.toList();
	}

}
