package br.com.postech.techchallange.domain.port.out;

import br.com.postech.techchallange.domain.model.AdminLogAcao;

public interface AdminLogAcaoRepositoryPort {
	void registrarLog(AdminLogAcao log);
}
