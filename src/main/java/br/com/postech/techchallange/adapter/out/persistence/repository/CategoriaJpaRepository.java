package br.com.postech.techchallange.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.postech.techchallange.adapter.out.persistence.entity.CategoriaEntity;

@Repository
public interface CategoriaJpaRepository extends JpaRepository<CategoriaEntity, Long> {
}
