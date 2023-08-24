package pl.umcs.workshop.image;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByGroupsId(Long groupId);

    List<Image> findAllByImageUserRoundRelationsRoundId(Long roundId);
}
