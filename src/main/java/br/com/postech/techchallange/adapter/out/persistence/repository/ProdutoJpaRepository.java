package br.com.postech.techchallange.adapter.out.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.postech.techchallange.adapter.out.persistence.entity.ProdutoEntity;

@Repository
public interface ProdutoJpaRepository extends JpaRepository<ProdutoEntity, Long> {

	public Optional<ProdutoEntity> findByIdCategoria(Long idCategoria);
	
}