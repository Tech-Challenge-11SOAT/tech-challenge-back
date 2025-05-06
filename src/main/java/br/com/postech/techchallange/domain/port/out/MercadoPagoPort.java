package br.com.postech.techchallange.domain.port.out;

import br.com.postech.techchallange.domain.model.Pagamento;

public interface MercadoPagoPort {
	String gerarQRCode(Pagamento pagamento);

	Pagamento criarPreferenciaPagamento(Pagamento pagamento);

}
