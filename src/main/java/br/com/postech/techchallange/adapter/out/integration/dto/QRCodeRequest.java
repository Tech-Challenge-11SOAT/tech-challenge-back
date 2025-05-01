package br.com.postech.techchallange.adapter.out.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QRCodeRequest {
	private String external_reference;
	private String title;
	private String description;
	private BigDecimal total_amount;
	private List<Item> items;
	private String notification_url;

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Item {
		private String sku_number;
		private String category;
		private String title;
		private String description;
		private String unit_measure;
		private Integer quantity;
		private String currency_id;
		private BigDecimal unit_price;
		private BigDecimal total_amount;
	}
}
