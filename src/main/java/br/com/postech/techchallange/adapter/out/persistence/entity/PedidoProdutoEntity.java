package br.com.postech.techchallange.adapter.out.persistence.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pedido_produto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoProdutoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_pedido_produto")
	private Long id;

	@Column(name = "id_pedido")
	private Long idPedido;

	@Column(name = "id_produto")
	private Long idProduto;

	@Column(name = "quantidade")
	private Integer quantidade;

	@Column(name = "preco_unitario")
	private BigDecimal precoUnitario;
}