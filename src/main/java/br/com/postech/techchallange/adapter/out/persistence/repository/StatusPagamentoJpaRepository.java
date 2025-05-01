package br.com.postech.techchallange.adapter.out.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.postech.techchallange.adapter.out.persistence.entity.StatusPagamentoEntity;
import br.com.postech.techchallange.domain.model.StatusPagamento;

public interface StatusPagamentoJpaRepository extends JpaRepository<StatusPagamentoEntity, Long> {
	Optional<StatusPagamento> findByNomeStatusIgnoreCase(String nomeStatus);
}
