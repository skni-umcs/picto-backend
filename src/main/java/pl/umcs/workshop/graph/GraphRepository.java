package pl.umcs.workshop.graph;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.umcs.workshop.graph.GraphEntity;

public interface GraphRepository extends JpaRepository<GraphEntity, Long> {}
