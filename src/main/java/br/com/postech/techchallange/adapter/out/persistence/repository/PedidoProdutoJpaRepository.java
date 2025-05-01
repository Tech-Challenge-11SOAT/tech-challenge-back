package br.com.postech.techchallange.adapter.out.persistence.repository;

import br.com.postech.techchallange.adapter.out.persistence.entity.PedidoProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoProdutoJpaRepository extends JpaRepository<PedidoProdutoEntity, Long> {
	List<PedidoProdutoEntity> findByIdPedido(Long idPedido);
}
