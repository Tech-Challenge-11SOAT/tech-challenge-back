package br.com.postech.techchallange.adapter.out.persistence.adapter;

import br.com.postech.techchallange.adapter.out.persistence.mapper.ClienteMapper;
import br.com.postech.techchallange.adapter.out.persistence.repository.ClienteJpaRepository;
import br.com.postech.techchallange.domain.model.Cliente;
import br.com.postech.techchallange.domain.port.out.ClienteRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ClienteRepositoryAdapter implements ClienteRepositoryPort {

    private final ClienteJpaRepository jpaRepository;

    public ClienteRepositoryAdapter(ClienteJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Cliente salvar(Cliente cliente) {
        var entity = ClienteMapper.toEntity(cliente);
        return ClienteMapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        return jpaRepository.findById(id).map(ClienteMapper::toDomain);
    }

    @Override
    public List<Cliente> listarTodos() {
        return jpaRepository.findAll()
                .stream()
                .map(ClienteMapper::toDomain)
                .toList();
    }
}
