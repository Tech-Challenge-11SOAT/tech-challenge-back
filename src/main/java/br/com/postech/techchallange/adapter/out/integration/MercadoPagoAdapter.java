package br.com.postech.techchallange.adapter.out.integration;

import org.springframework.stereotype.Component;

import br.com.postech.techchallange.domain.exception.BusinessException;
import br.com.postech.techchallange.domain.model.Pagamento;
import br.com.postech.techchallange.domain.port.out.MercadoPagoPort;

@Component
public class MercadoPagoAdapter implements MercadoPagoPort {

	// private final IntegracaoMercadoPagoConfig config;

	/**
	 * public MercadoPagoAdapter(IntegracaoMercadoPagoConfig config) { this.config =
	 * config; MercadoPagoConfig.setAccessToken(config.accessToken()); }
	 **/

	@Override
	public Pagamento criarPreferenciaPagamento(Pagamento pagamento) {
		try {/**
				 * PreferenceClient client = new PreferenceClient();
				 * 
				 * PreferenceItemRequest item = PreferenceItemRequest.builder() .title("Pedido
				 * #" + pagamento.getIdPedido()) .quantity(1)
				 * .unitPrice(pagamento.getValorTotal()) .currencyId("BRL") .build();
				 * 
				 * PreferenceRequest preferenceRequest = PreferenceRequest.builder()
				 * .items(Collections.singletonList(item))
				 * .notificationUrl(config.notificationUrl())
				 * .externalReference(String.valueOf(pagamento.getIdPedido())) .build();
				 * 
				 * Preference preference = client.create(preferenceRequest);
				 **/
			return pagamento;

		} catch (Exception e) {
			throw new BusinessException("Erro ao integrar com Mercado Pago: " + e.getMessage());
		}
	}

	@Override
	public String gerarQRCode(Pagamento pagamento) {
		return null;
	}
}
