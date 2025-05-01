package br.com.postech.techchallange.domain.enums;

public enum StatusPedidoEnum {
	RECEBIDO("RECEBIDO"),
	EM_ANDAMENTO("EM_ANDAMENTO"),
	FINALIZADO("FINALIZADO");
	
	private String status;

	private StatusPedidoEnum(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public static StatusPagamentoEnum of(String status) {
		if (status == null) return null;
		return StatusPagamentoEnum.of(status);
	}

}
