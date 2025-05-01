package br.com.postech.techchallange.adapter.out.persistence.entity;

import br.com.postech.techchallange.domain.enums.StatusPagamentoEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "status_pagamento")
@Data
@Builder
public class StatusPagamentoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idStatusPagamento;

	@Enumerated(EnumType.STRING)
	@Column(name = "nome_status", nullable = false)
	private StatusPagamentoEnum nomeStatus;
}
