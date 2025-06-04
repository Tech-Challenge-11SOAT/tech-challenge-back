package br.com.postech.techchallange.adapter.out.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import br.com.postech.techchallange.adapter.out.persistence.mapper.CategoriaMapper;
import br.com.postech.techchallange.adapter.out.persistence.repository.CategoriaJpaRepository;
import br.com.postech.techchallange.domain.model.Categoria;
import br.com.postech.techchallange.domain.port.out.CategoriaRepositoryPort;

@Component
public class CategoriaRepositoryAdapter implements CategoriaRepositoryPort {

	private final CategoriaJpaRepository categoriaJpaRepository;

	public CategoriaRepositoryAdapter(CategoriaJpaRepository categoriaJpaRepository) {
		this.categoriaJpaRepository = categoriaJpaRepository;
	}

	@Override
	public Optional<Categoria> buscarPorId(Long id) {
		return categoriaJpaRepository.findById(id).map(CategoriaMapper::toDomain);
	}
}
