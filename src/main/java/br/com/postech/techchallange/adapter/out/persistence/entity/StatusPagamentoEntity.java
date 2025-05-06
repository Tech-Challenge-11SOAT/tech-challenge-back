package br.com.postech.techchallange.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "status_pagamento")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusPagamentoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idStatusPagamento;

	@Column(name = "nome_status", nullable = false)
	private String nomeStatus;
}
