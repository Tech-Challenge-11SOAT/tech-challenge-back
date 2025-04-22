package br.com.postech.techchallange.adapter.out.persistence.repository;

import br.com.postech.techchallange.adapter.out.persistence.entity.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoJpaRepository extends JpaRepository<PedidoEntity, Long> {
}
