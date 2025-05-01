package br.com.postech.techchallange.adapter.out.integration;

import br.com.postech.techchallange.adapter.out.integration.dto.QRCodeRequest;
import br.com.postech.techchallange.adapter.out.integration.dto.QRCodeRequest.Item;
import br.com.postech.techchallange.adapter.out.integration.dto.QRCodeResponse;
import br.com.postech.techchallange.domain.model.Pagamento;
import br.com.postech.techchallange.domain.port.out.MercadoPagoPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class MercadoPagoAdapter implements MercadoPagoPort {

	private final MercadoPagoFeignClient mercadoPagoFeignClient;

	@Value("${mercado.pago.access.token}")
	private String accessToken;

	@Value("${mercado.pago.user.id}")
	private String userId;

	@Value("${mercado.pago.external.pos.id}")
	private String externalPosId;

	@Value("${mercado.pago.notification.url}")
	private String notificationUrl;

	@Override
	public String gerarQRCode(Pagamento pagamento) {
		Item item = Item.builder()
				.sku_number("SKU123")
				.category("services")
				.title("Pagamento Pedido #" + pagamento.getIdPedido())
				.description("Pagamento referente ao pedido #" + pagamento.getIdPedido())
				.unit_measure("unit")
				.quantity(1)
				.currency_id("BRL")
				.unit_price(pagamento.getValorTotal())
				.total_amount(pagamento.getValorTotal())
				.build();

		QRCodeRequest request = QRCodeRequest.builder()
				.external_reference("pedido_" + pagamento.getIdPedido())
				.title("Pagamento Pedido #" + pagamento.getIdPedido())
				.description("Pagamento via QR Code")
				.total_amount(pagamento.getValorTotal())
				.items(Collections.singletonList(item))
				.notification_url(notificationUrl)
				.build();

		QRCodeResponse response = mercadoPagoFeignClient.gerarQRCode(
				this.getAuthorization(accessToken),
				userId,
				externalPosId,
				request
			);

		return response.getQr_data();
	}
	
	private String getAuthorization(String accessToken) {
		return "Bearer " + accessToken;
	}
}
