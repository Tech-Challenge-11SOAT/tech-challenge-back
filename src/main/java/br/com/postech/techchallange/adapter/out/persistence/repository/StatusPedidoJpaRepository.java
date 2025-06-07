package br.com.postech.techchallange.adapter.out.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.postech.techchallange.adapter.out.persistence.entity.StatusPedidoEntity;

@Repository
public interface StatusPedidoJpaRepository extends JpaRepository<StatusPedidoEntity, Long> {

    Optional<StatusPedidoEntity> findByNomeStatusIgnoreCase(String nomeStatus);
}
