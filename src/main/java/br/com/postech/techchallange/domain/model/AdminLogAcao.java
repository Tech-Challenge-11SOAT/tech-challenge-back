package br.com.postech.techchallange.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminLogAcao {
	private Long id;
	private Long idAdmin;
	private String acao;
	private String recursoAfetado;
	private Long idRecurso;
	private LocalDateTime dataAcao;
}
