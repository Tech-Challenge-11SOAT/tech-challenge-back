package br.com.postech.techchallange.domain.exception;

@SuppressWarnings("serial")
public class InvalidPaymentAmountException extends BusinessException {
	public InvalidPaymentAmountException() {
		super("Valor do pagamento não condiz com o total do pedido.");
	}
}