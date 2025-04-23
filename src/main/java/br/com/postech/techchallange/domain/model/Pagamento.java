package br.com.postech.techchallange.domain.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pagamento {
	private Long id;
	private Long idPedido;
	private BigDecimal valorTotal;
	private String metodoPagamento;
	private Long idStatusPagamento;
	private LocalDateTime dataPagamento;
}
