package pl.umcs.workshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.umcs.workshop.utils.Graph;

@SpringBootApplication
public class WorkshopApplication {
    public static void main(String[] args) {
        Graph.printGraph();
//        SpringApplication.run(WorkshopApplication.class, args);
    }
}