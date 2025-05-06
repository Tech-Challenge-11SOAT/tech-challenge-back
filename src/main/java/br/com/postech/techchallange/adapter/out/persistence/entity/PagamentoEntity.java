package br.com.postech.techchallange.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamento")
@Data
@Builder
public class PagamentoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idPagamento;

	private Long idPedido;
	private BigDecimal valorTotal;
	private String metodoPagamento;
	private Long idStatusPagamento;
	private LocalDateTime dataPagamento;
	private String initPoint;
}
