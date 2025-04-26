package br.com.postech.techchallange.adapter.in.rest.response;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {

	private int status;
	private String error;
	private String message;
	private LocalDateTime timestamp;

	public ErrorResponse(int status, String error, String message) {
		this.status = status;
		this.error = error;
		this.message = message;
		this.timestamp = LocalDateTime.now();
	}

}
