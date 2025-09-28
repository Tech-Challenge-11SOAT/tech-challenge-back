package br.com.postech.techchallange;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.env.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@EnableFeignClients
@RequiredArgsConstructor
public class TechchallangeApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(TechchallangeApplication.class);

    private final Environment env;

    public static void main(String[] args) {
        SpringApplication.run(TechchallangeApplication.class, args);
    }

    @Override
    public void run(String... args) {
        String[] activeProfiles = env.getActiveProfiles();

        if (activeProfiles.length == 0) {
            log.warn("⚠Nenhum profile ativo encontrado. Usando configuração default.");
        } else {
            log.info("Profiles ativos: {}", String.join(", ", activeProfiles));
        }
    }
}
