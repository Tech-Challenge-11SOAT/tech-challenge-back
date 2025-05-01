package br.com.postech.techchallange.adapter.out.integration.dto;

import lombok.Data;

@Data
public class QRCodeResponse {
	private String qr_data;
	private String qr_code_base64;
}
