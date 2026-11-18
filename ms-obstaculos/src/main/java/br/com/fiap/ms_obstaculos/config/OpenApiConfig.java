package br.com.fiap.ms_obstaculos.config;

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
                        .title("Microsserviço de Obstáculos")
                        .description("API do RotaLivre responsável pelos obstáculos informados pela comunidade "
                                + "e pelos sensores IoT, que atualiza a situação dos trechos no ms-trechos")
                        .version("v1"))

                .servers(List.of(
                        new Server().url("/ms-obstaculos")
                ));
    }
}
