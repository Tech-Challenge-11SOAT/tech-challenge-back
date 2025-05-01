package br.com.postech.techchallange.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "status_pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusPedidoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_status_pedido")
	private Long idStatusPedido;

	@Column(name = "nome_status")
	private String nomeStatus;
}
