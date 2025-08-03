package br.com.postech.techchallange.infra.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "mercadopago")
@Getter
@Setter
public class MercadoPagoOptionsConfig {

    private Api api = new Api();
    private Options options = new Options();

    @Getter
    @Setter
    public static class Api {
        private String baseUrl;
        private String accessToken;
        private String publicKey;
        private Integer timeout;
    }

    @Getter
    @Setter
    public static class Options {
        private Boolean testMode = true;
    }
}
