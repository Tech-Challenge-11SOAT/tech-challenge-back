package br.com.postech.techchallange.adapter.out.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

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
}
