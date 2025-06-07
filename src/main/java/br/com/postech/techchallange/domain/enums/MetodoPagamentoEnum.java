package br.com.postech.techchallange.domain.enums;

public enum MetodoPagamentoEnum {
    PIX("PIX"),
    CARTAO("CARTAO"),
    DINHEIRO("DINHEIRO");

    private String value;

    private MetodoPagamentoEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static MetodoPagamentoEnum of(String value) {
        if (value == null) {
            return null;
        }
        return MetodoPagamentoEnum.valueOf(value);
    }
}
