package pl.umcs.workshop.graph;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GraphRepository extends JpaRepository<GraphEntity, Long> {}
