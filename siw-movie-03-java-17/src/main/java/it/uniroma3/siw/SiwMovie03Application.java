package it.uniroma3.siw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SiwMovie03Application {

	public static void main(String[] args) {
		SpringApplication.run(SiwMovie03Application.class, args);
	}
}
