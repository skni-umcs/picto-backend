package pl.umcs.workshop.round;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoundRepository extends JpaRepository<Round, Long> {

    Round findOneByGameIdAndUserOneIdAndUserTwoIdAndGeneration(Long gameId, Long userOneId, Long userTwoId, int generation);

    default Round getNextRound(Long gameId, Long userId, int generation) {
        return findOneByGameIdAndUserOneIdAndUserTwoIdAndGeneration(gameId, userId, userId, generation);
    }
}
