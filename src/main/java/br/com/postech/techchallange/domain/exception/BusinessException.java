package br.com.postech.techchallange.domain.exception;

@SuppressWarnings("serial")
public class BusinessException extends RuntimeException {

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}
}
