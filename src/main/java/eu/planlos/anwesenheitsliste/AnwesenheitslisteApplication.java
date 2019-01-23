package eu.planlos.anwesenheitsliste;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={
		"eu.planlos.anwesenheitsliste",
		"org.springframework.security.web.access"}) //, exclude = { ErrorMvcAutoConfiguration.class }
public class AnwesenheitslisteApplication {

	//TODO implement /error site
	public static void main(String[] args) {
		SpringApplication.run(AnwesenheitslisteApplication.class, args);
	}
}
