	package br.com.postech.techchallange.adapter.out.integration;

	import java.util.Collections;

	import org.springframework.beans.factory.annotation.Value;
	import org.springframework.stereotype.Component;

	import com.mercadopago.MercadoPagoConfig;
	import com.mercadopago.client.preference.PreferenceClient;
	import com.mercadopago.client.preference.PreferenceItemRequest;
	import com.mercadopago.client.preference.PreferenceRequest;
	import com.mercadopago.exceptions.MPApiException;
	import com.mercadopago.exceptions.MPException;
	import com.mercadopago.resources.preference.Preference;

	import br.com.postech.techchallange.domain.model.Pagamento;
	import br.com.postech.techchallange.domain.port.out.MercadoPagoPort;
	import lombok.RequiredArgsConstructor;

	@Component
	@RequiredArgsConstructor
	public class MercadoPagoAdapter implements MercadoPagoPort {

	@Value("${mercadopago.access-token}")
	private String accessToken;

	@Override
	public String gerarQRCode(Pagamento pagamento) {
		try {
			MercadoPagoConfig.setAccessToken(accessToken);

			PreferenceItemRequest item =
				PreferenceItemRequest.builder()
					.title("Pagamento Pedido #" + pagamento.getIdPedido())
					.quantity(1)
					.unitPrice(pagamento.getValorTotal())
					.currencyId("BRL")
					.build();

			PreferenceClient client = new PreferenceClient();
			PreferenceRequest request =
				PreferenceRequest.builder()
					.items(Collections.singletonList(item))
					.externalReference(pagamento.getIdPedido().toString())
					.build();

			Preference preference = client.create(request);

			return preference.getInitPoint();

		} catch (MPApiException | MPException e) {
			throw new RuntimeException("Erro ao gerar QR Code no Mercado Pago: " + e.getMessage(), e);
		}
	}
	}
