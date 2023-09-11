package pl.umcs.workshop.round;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.umcs.workshop.game.Game;

public interface RoundRepository extends JpaRepository<Round, Long> {

  Round findOneByGameIdAndUserOneIdAndGeneration(Long gameId, Long userOneId, int generation);

  Round findOneByGameIdAndUserTwoIdAndGeneration(Long gameId, Long userTwoId, int generation);

  default Round getNextRound(Long gameId, Long userId, int generation) {
    Round b = findOneByGameIdAndUserOneIdAndGeneration(gameId, userId, generation);
    Round c = findOneByGameIdAndUserTwoIdAndGeneration(gameId, userId, generation);
    return b == null ? c : b;
  }

  List<Round> findAllByGame(Game game);
}
