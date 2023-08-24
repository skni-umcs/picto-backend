package pl.umcs.workshop.user;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.umcs.workshop.game.Game;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByGame(Game game);

    List<User> findAllByGameId(Long gameId);
}
