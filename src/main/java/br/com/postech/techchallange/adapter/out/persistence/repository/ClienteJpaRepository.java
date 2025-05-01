package br.com.postech.techchallange.adapter.out.persistence.repository;

import br.com.postech.techchallange.adapter.out.persistence.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.nio.channels.FileChannel;

public interface ClienteJpaRepository extends JpaRepository<ClienteEntity, Long> {

}
