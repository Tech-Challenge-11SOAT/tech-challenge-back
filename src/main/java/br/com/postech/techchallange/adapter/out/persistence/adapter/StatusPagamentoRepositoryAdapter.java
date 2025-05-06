package br.com.postech.techchallange.adapter.out.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import br.com.postech.techchallange.adapter.out.persistence.mapper.StatusPagamentoMapper;
import br.com.postech.techchallange.adapter.out.persistence.repository.StatusPagamentoJpaRepository;
import br.com.postech.techchallange.domain.model.StatusPagamento;
import br.com.postech.techchallange.domain.port.out.StatusPagamentoRepositoryPort;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StatusPagamentoRepositoryAdapter implements StatusPagamentoRepositoryPort {

	private final StatusPagamentoJpaRepository repository;

	@Override
	public Optional<StatusPagamento> buscarPorId(Long id) {
		return repository.findById(id)
				.map(StatusPagamentoMapper::toDomain);
	}

	@Override
	public Optional<StatusPagamento> buscarStatusPagamentoPorStatus(String statusParam) {
		return repository.findByNomeStatusIgnoreCase(statusParam)
				.map(StatusPagamentoMapper::toDomain);
	}
	
	@Override
	public List<StatusPagamento> listarTodos() {
		return repository.findAll()
				.stream()
				.map(StatusPagamentoMapper::toDomain)
				.toList();
	}

}
