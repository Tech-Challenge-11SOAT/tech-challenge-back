package br.com.postech.techchallange.domain.port.in;

public interface AlterarSenhaAdminUseCase {
	void alterarSenha(String email, String senhaAtual, String novaSenha);
}
