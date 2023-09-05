package pl.umcs.workshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.umcs.workshop.user.User;
import pl.umcs.workshop.utils.Graph;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class WorkshopApplication {
    public static void main(String[] args) {
        SpringApplication.run(WorkshopApplication.class, args);
    }
}