package pl.umcs.workshop.user;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.umcs.workshop.game.Game;

public interface UserRepository extends JpaRepository<User, Long> {
  List<User> findAllByGame(Game game);

  List<User> findAllByGameId(Long gameId);
}
