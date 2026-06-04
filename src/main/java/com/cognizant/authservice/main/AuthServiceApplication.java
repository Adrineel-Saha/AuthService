package com.cognizant.authservice.main;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@SpringBootApplication(scanBasePackages = "com.cognizant.authservice.*")
@EnableJpaRepositories(basePackages = "com.cognizant.authservice.repositories")
@EntityScan(basePackages = "com.cognizant.authservice.entities")
@EnableDiscoveryClient(autoRegister=true)
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.servers(List.of(
						new Server().url("http://localhost:9191").description("API Gateway")
				));
	}
}
