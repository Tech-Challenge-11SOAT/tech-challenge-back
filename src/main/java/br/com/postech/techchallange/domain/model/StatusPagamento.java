package br.com.postech.techchallange.domain.model;

import br.com.postech.techchallange.domain.enums.StatusPagamentoEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatusPagamento {
	private Long idStatusPagamento;
	private StatusPagamentoEnum nomeStatus;
}
