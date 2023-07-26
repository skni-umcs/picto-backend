package pl.umcs.workshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class WorkshopApplication {
	public static void main(String[] args) {
		SpringApplication.run(WorkshopApplication.class, args);
	}
}