package br.com.postech.techchallange.domain.enums;

public enum StatusPedidoEnum {
    RECEBIDO("RECEBIDO"),
    RECEBIDO_NAO_PAGO("RECEBIDO - Não pago"),
    EM_ANDAMENTO("EM_ANDAMENTO"),
    FINALIZADO("FINALIZADO"),
    CANCELADO("CANCELADO");

    private String status;

    private StatusPedidoEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static StatusPagamentoEnum of(String status) {
        if (status == null) {
            return null;
        }
        return StatusPagamentoEnum.of(status);
    }

}
