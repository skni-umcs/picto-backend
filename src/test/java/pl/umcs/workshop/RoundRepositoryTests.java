package pl.umcs.workshop;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import pl.umcs.workshop.round.Round;
import pl.umcs.workshop.round.RoundRepository;

import java.util.Optional;

@DataJpaTest
@Rollback(value = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RoundRepositoryTests {
    @Autowired
    private RoundRepository roundRepository;

    @Test
    @Order(value = 1)
    public void saveRoundTest() {
        Round round = Round.builder()
                .gameId(3)
                .generation(15)
                .userOneId(34)
                .userTwoId(17)
                .userOneAnswerTime(3.52)
                .userTwoAnswerTime(2.61)
                .topic(4)
                .imageSelected(4)
                .build();

        roundRepository.save(round);

        Assertions.assertThat(round.getId()).isGreaterThan(0);
    }

    @Test
    @Order(value = 2)
    public void getRoundTest() {
        Round round = roundRepository.findById(1).orElse(null);

        assert round != null;
        Assertions.assertThat(round.getId()).isEqualTo(1);
    }

    @Test
    @Order(value = 3)
    public void updateRoundTest() {
        Round round = roundRepository.findById(1).orElse(null);

        assert round != null;
        round.setGameId(15);
    }

    @Test
    @Order(value = 4)
    public void deleteRoundTest() {
        Round round = roundRepository.findById(1).orElse(null);

        assert round != null;
        roundRepository.delete(round);

        Round roundCheck = null;
        Optional<Round> roundOptional = roundRepository.findById(1);

        if (roundOptional.isPresent()) {
            roundCheck = roundOptional.get();
        }

        Assertions.assertThat(roundCheck).isNull();
    }
}
