package br.com.postech.techchallange.adapter.out.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import br.com.postech.techchallange.adapter.out.persistence.mapper.PagamentoMapper;
import br.com.postech.techchallange.adapter.out.persistence.repository.PagamentoJpaRepository;
import br.com.postech.techchallange.domain.model.Pagamento;
import br.com.postech.techchallange.domain.port.out.PagamentoRepositoryPort;

@Component
public class PagamentoRepositoryAdapter implements PagamentoRepositoryPort {

    private final PagamentoJpaRepository repository;

    public PagamentoRepositoryAdapter(PagamentoJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Pagamento salvar(Pagamento pagamento) {
        return PagamentoMapper.toDomain(repository.save(PagamentoMapper.toEntity(pagamento)));
    }

    @Override
    public Optional<Pagamento> buscarPorId(Long id) {
        return repository.findById(id).map(PagamentoMapper::toDomain);
    }

    @Override
    public List<Pagamento> listarTodos() {
        return repository.findAll().stream().map(PagamentoMapper::toDomain).toList();
    }

    @Override
    public Optional<Pagamento> buscarPorIdPedido(Long idPedido) {
        return repository.findAll().stream()
                .filter(e -> e.getIdPedido().equals(idPedido))
                .findFirst()
                .map(PagamentoMapper::toDomain);
    }
}
