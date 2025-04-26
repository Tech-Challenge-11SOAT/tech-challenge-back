package br.com.postech.techchallange.domain.port.in;

import br.com.postech.techchallange.domain.model.AdminUser;

public interface AutenticarAdminUseCase {
	AdminUser autenticar(String email, String senha);
}
