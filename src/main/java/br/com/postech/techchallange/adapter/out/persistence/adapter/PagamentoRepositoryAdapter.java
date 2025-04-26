package br.com.postech.techchallange.adapter.out.persistence.adapter;

import br.com.postech.techchallange.domain.model.Pagamento;
import br.com.postech.techchallange.domain.port.out.PagamentoRepositoryPort;
import br.com.postech.techchallange.adapter.out.persistence.mapper.PagamentoMapper;
import br.com.postech.techchallange.adapter.out.persistence.repository.PagamentoJpaRepository;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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
}
