package br.com.postech.techchallange.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusPedido {
	private Long idStatusPedido;
	private String nomeStatus;
}