package pl.umcs.workshop.user;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import pl.umcs.workshop.game.Game;

public interface UserRepository extends JpaRepository<User, Long> {
  List<User> findAllByGame(Game game);

  List<User> findAllByGameId(Long gameId);

  @Modifying
  @Transactional
  @Query("update User u set u.generation = :generation where u.id = :id")
  void refresh(@Param("id") Long id, @Param("generation") int generation);
}
