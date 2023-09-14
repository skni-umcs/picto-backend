package pl.umcs.workshop.user;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.umcs.workshop.game.Game;

public interface UserRepository extends JpaRepository<User, Long> {
  List<User> findAllByGame(Game game);

  List<User> findAllByGameId(Long gameId);

  @Modifying
  @Query("UPDATE User u SET u.generation = u.generation + 1 WHERE u.id = :userId")
  void incrementGeneration(@Param("userId") Long userId);
}
