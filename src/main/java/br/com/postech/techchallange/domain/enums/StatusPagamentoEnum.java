package br.com.postech.techchallange.domain.enums;

public enum StatusPagamentoEnum {
	PENDENTE("PENDENTE"),
	ERRO("ERRO"),
	FINALIZADO("FINALIZADO");
	
	private String status;
	
	private StatusPagamentoEnum(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}
	
	public static StatusPagamentoEnum of(String status) {
		if (status == null) return null;
		
		return StatusPagamentoEnum.valueOf(status);
	}
	
}
