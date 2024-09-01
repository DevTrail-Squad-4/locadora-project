package br.edu.solutis.dev.trail.locadora;

import br.edu.solutis.dev.trail.locadora.model.dto.pessoa.MotoristaDTO;
import br.edu.solutis.dev.trail.locadora.model.enums.SexoEnum;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;


@SpringBootApplication
public class LocadoraApplication {

	public static void main(String[] args) throws IOException, InterruptedException {
		SpringApplication.run(LocadoraApplication.class, args);


	}}


