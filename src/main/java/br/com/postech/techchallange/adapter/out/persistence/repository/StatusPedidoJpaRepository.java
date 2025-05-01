package br.com.postech.techchallange.adapter.out.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.postech.techchallange.adapter.out.persistence.entity.StatusPedidoEntity;

public interface StatusPedidoJpaRepository extends JpaRepository<StatusPedidoEntity, Long> {
	Optional<StatusPedidoEntity> findByNomeStatusIgnoreCase(String nomeStatus);
}
