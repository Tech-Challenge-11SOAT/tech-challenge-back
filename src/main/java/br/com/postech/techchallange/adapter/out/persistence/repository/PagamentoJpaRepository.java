package br.com.postech.techchallange.adapter.out.persistence.repository;

import br.com.postech.techchallange.adapter.out.persistence.entity.PagamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoJpaRepository extends JpaRepository<PagamentoEntity, Long> {
}
