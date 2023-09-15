package pl.umcs.workshop.round;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.umcs.workshop.game.Game;

public interface RoundRepository extends JpaRepository<Round, Long> {

  Round findOneByGameIdAndUserOneIdAndGenerationOrGameIdAndUserTwoIdAndGeneration(
      Long gameIdOne,
      Long userOneId,
      int generationOne,
      Long gameIdTwo,
      Long userTwo,
      int generationTwo);

  Round findOneByGameIdAndUserTwoIdAndGeneration(Long gameId, Long userTwoId, int generation);

  default Round getNextRound(Long gameId, Long userId, int generation) {
    //    Round b = findOneByGameIdAndUserOneIdAndGeneration(gameId, userId, generation);
    //    Round c = findOneByGameIdAndUserTwoIdAndGeneration(gameId, userId, generation);
    //    return b == null ? c : b;
    return findOneByGameIdAndUserOneIdAndGenerationOrGameIdAndUserTwoIdAndGeneration(
        gameId, userId, generation, gameId, userId, generation);
  }

  List<Round> findAllByGame(Game game);

  List<Round> findAllByUserOneIdOrUserTwoId(Long userOneId, Long userTwoId);

  default List<Round> findAllByUserId(Long userId) {
    return findAllByUserOneIdOrUserTwoId(userId, userId);
  }
}
