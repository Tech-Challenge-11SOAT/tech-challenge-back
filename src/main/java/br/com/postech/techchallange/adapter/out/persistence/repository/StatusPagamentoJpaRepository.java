package br.com.postech.techchallange.adapter.out.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.postech.techchallange.adapter.out.persistence.entity.StatusPagamentoEntity;

public interface StatusPagamentoJpaRepository extends JpaRepository<StatusPagamentoEntity, Long> {
	Optional<StatusPagamentoEntity> findByNomeStatusIgnoreCase(String nomeStatus);
}
