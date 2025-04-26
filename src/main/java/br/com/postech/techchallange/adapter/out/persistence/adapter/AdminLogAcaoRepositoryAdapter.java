package br.com.postech.techchallange.adapter.out.persistence.adapter;

import br.com.postech.techchallange.domain.model.AdminLogAcao;
import br.com.postech.techchallange.domain.port.out.AdminLogAcaoRepositoryPort;
import br.com.postech.techchallange.adapter.out.persistence.repository.AdminLogAcaoJpaRepository;
import br.com.postech.techchallange.adapter.out.persistence.repository.AdminUserJpaRepository;
import br.com.postech.techchallange.adapter.out.persistence.mapper.AdminLogAcaoMapper;
import org.springframework.stereotype.Component;

@Component
public class AdminLogAcaoRepositoryAdapter implements AdminLogAcaoRepositoryPort {

	private final AdminLogAcaoJpaRepository logRepository;
	private final AdminUserJpaRepository userRepository;

	public AdminLogAcaoRepositoryAdapter(AdminLogAcaoJpaRepository logRepository,
			AdminUserJpaRepository userRepository) {
		this.logRepository = logRepository;
		this.userRepository = userRepository;
	}

	@Override
	public void registrarLog(AdminLogAcao log) {
		var adminUser = userRepository.findById(log.getIdAdmin())
				.orElseThrow(() -> new RuntimeException("Admin não encontrado para log"));

		logRepository.save(AdminLogAcaoMapper.toEntity(log, adminUser));
	}
}
