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

import java.util.List;
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
                .gameId(3L)
                .generation(15)
                .userOneId(34L)
                .userTwoId(17L)
                .userOneAnswerTime(3.52)
                .userTwoAnswerTime(2.61)
                .topic(4L)
                .imageSelected(4L)
                .build();

        roundRepository.save(round);

        Assertions.assertThat(round.getId()).isGreaterThan(0);
    }

    @Test
    @Order(value = 2)
    public void getRoundTest() {
        Round round = roundRepository.findById(1L).orElse(null);

        Assertions.assertThat(round).isNotNull();
        Assertions.assertThat(round.getId()).isEqualTo(1);
    }

    @Test
    @Order(value = 3)
    public void getListOfAllRoundsTest() {
        Round round = Round.builder()
                .gameId(1L)
                .generation(4)
                .userOneId(8L)
                .userTwoId(4L)
                .userOneAnswerTime(6.17)
                .userTwoAnswerTime(1.53)
                .topic(3L)
                .imageSelected(7L)
                .build();

        roundRepository.save(round);

        List<Round> rounds = roundRepository.findAll();

        Assertions.assertThat(rounds.size()).isEqualTo(2);
    }

    @Test
    @Order(value = 4)
    public void updateRoundTest() {
        Round round = roundRepository.findById(1L).orElse(null);

        assert round != null;
        Assertions.assertThat(round.getImageSelected()).isEqualTo(4);

        round.setImageSelected(7L);

        Round savedRound = roundRepository.save(round);

        Assertions.assertThat(round.getImageSelected()).isEqualTo(7);
        Assertions.assertThat(savedRound.getImageSelected()).isEqualTo(7);
        Assertions.assertThat(round.getId()).isEqualTo(savedRound.getId());
    }

    @Test
    @Order(value = 5)
    public void deleteRoundTest() {
        Round round = roundRepository.findById(1L).orElse(null);

        Assertions.assertThat(round).isNotNull();
        roundRepository.delete(round);

        Round roundCheck = null;
        Optional<Round> roundOptional = roundRepository.findById(1L);

        if (roundOptional.isPresent()) {
            roundCheck = roundOptional.get();
        }

        Assertions.assertThat(roundCheck).isNull();
    }
}
