package com.backend.aula09;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "API de Adoção de Animais",
				version = "1.0.0",
				description = "API para gerenciar adoções, usuários e animais em um sistema de adoção.",
				contact = @Contact(name = "Equipe de Suporte", email = "helpet@adocaoanimais.com")
		),
		servers = {
				@Server(url = "http://localhost:8080", description = "Servidor de desenvolvimento"),
		}
)
public class Aula09Application {

	public static void main(String[] args) {
		SpringApplication.run(Aula09Application.class, args);
	}

}
