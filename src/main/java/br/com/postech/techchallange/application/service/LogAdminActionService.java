package br.com.postech.techchallange.application.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import br.com.postech.techchallange.domain.model.AdminLogAcao;
import br.com.postech.techchallange.domain.port.in.LogAdminActionUseCase;
import br.com.postech.techchallange.domain.port.out.AdminLogAcaoRepositoryPort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogAdminActionService implements LogAdminActionUseCase {

    private final AdminLogAcaoRepositoryPort adminLogRepository;

    @Override
    public void registrar(Long idAdmin, String acao, String recursoAfetado, Long idRecursoAfetado) {
        if (idAdmin == null) {
            throw new IllegalArgumentException("ID do admin não pode ser nulo");
        }
        if (acao == null) {
            throw new IllegalArgumentException("Ação não pode ser nula");
        }
        if (recursoAfetado == null) {
            throw new IllegalArgumentException("Recurso afetado não pode ser nulo");
        }

        AdminLogAcao log = AdminLogAcao.builder()
                .idAdmin(idAdmin)
                .acao(acao)
                .recursoAfetado(recursoAfetado)
                .idRecurso(idRecursoAfetado)
                .dataAcao(LocalDateTime.now())
                .build();

        adminLogRepository.registrarLog(log);
    }
}
