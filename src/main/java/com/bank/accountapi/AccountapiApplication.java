package com.bank.accountapi;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@OpenAPIDefinition(info = @Info(title = "Account api", version = "1.0", description = "Documentation Account api v1.0"))
public class AccountapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountapiApplication.class, args);
	}

	@Bean
	@Primary
	public ObjectMapper objectMapper() {
		JavaTimeModule module = new JavaTimeModule();
		return new ObjectMapper()
			.enable(DeserializationFeature.UNWRAP_ROOT_VALUE)
			.registerModule(module);
	}
}
