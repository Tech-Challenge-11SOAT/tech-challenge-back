package br.com.postech.techchallange.domain.port.in;

public interface ProcessarPagamentoUseCase {
	void salvarQRCode(String idPedido, String qrCode);

	String obterQRCode(String idPedido);
}