package br.com.postech.techchallange.domain.port.in;

public interface LogAdminActionUseCase {

	void registrar(Long idAdmin, String acao, String recursoAfetado, Long idRecursoAfetado);
}
