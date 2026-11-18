package br.com.fiap.ms_trechos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsTrechosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsTrechosApplication.class, args);
	}

}
