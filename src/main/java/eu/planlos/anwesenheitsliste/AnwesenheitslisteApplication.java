package eu.planlos.anwesenheitsliste;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={
		"eu.planlos.anwesenheitsliste",
		"org.springframework.security.web.access"})
public class AnwesenheitslisteApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnwesenheitslisteApplication.class, args);
	}
}
