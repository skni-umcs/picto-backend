package pl.umcs.workshop.round;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoundRepository extends JpaRepository<Round, Integer> {

    Round findOneByGameIdAndUserOneIdAndUserTwoIdAndGeneration(int gameId, int userOneId, int userTwoId, int generation);

    default Round getNextRound(int gameId, int userId, int generation) {
        return findOneByGameIdAndUserOneIdAndUserTwoIdAndGeneration(gameId, userId, userId, generation);
    }
}
