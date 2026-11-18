package br.com.fiap.ms_trechos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Microsserviço de Trechos")
                        .description("API do RotaLivre responsável pelos trechos de calçada e pela situação "
                                + "de acessibilidade de cada um (LIVRE, RESTRITO ou BLOQUEADO)")
                        .version("v1"))

                .servers(List.of(
                        new Server().url("/ms-trechos")
                ));
    }
}
