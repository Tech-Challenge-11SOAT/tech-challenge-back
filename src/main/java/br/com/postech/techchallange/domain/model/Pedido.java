package br.com.postech.techchallange.domain.model;

import java.time.LocalDateTime;

public class Pedido {

	private Long id;
	private Long idCliente;
	private LocalDateTime dataPedido;
	private Long idStatusPedido;
	private LocalDateTime dataStatus;
	private Integer filaPedido;

	public Pedido(Long id, Long idCliente, LocalDateTime dataPedido, Long idStatusPedido, LocalDateTime dataStatus, Integer filaPedido) {
		this.id = id;
		this.idCliente = idCliente;
		this.dataPedido = dataPedido;
		this.idStatusPedido = idStatusPedido;
		this.dataStatus = dataStatus;
		this.filaPedido = filaPedido;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public LocalDateTime getDataPedido() {
		return dataPedido;
	}

	public void setDataPedido(LocalDateTime dataPedido) {
		this.dataPedido = dataPedido;
	}

	public Long getIdStatusPedido() {
		return idStatusPedido;
	}

	public void setIdStatusPedido(Long idStatusPedido) {
		this.idStatusPedido = idStatusPedido;
	}

	public LocalDateTime getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(LocalDateTime dataStatus) {
		this.dataStatus = dataStatus;
	}

	public Integer getFilaPedido() {
		return filaPedido;
	}

	public void setFilaPedido(Integer filaPedido) {
		this.filaPedido = filaPedido;
	}

	@Override
	public String toString() {
		return "Pedido [id=" + id + ", idCliente=" + idCliente + ", dataPedido=" + dataPedido + ", idStatusPedido="
				+ idStatusPedido + ", dataStatus=" + dataStatus + ", filaPedido=" + filaPedido + "]";
	}

}
