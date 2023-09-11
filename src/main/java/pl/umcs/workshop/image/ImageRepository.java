package pl.umcs.workshop.image;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
  List<Image> findAllByGroupsId(Long groupId);

  List<Image> findAllByImageUserRoundRelationsRoundIdAndImageUserRoundRelationsUserId(
      Long roundId, Long userId);

  default List<Image> findAllImagesForUser(Long roundId, Long userId) {
    return findAllByImageUserRoundRelationsRoundIdAndImageUserRoundRelationsUserId(roundId, userId);
  }

  // TODO: symbols or something idk the only thing I want to commit is suicide
  //  default List<Image> findAllSymbolsForUser(Long roundId, Long userId) {
  //    return findAllByImageUserRoundRelationsRoundIdAndImageUserRoundRelationsUserId(roundId,
  // userId);
  //  }
}
