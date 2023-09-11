package pl.umcs.workshop.relation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageUserRoundRelationRepository
    extends JpaRepository<ImageUserRoundRelation, Long> {}
