package pl.umcs.workshop;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import pl.umcs.workshop.round.Round;
import pl.umcs.workshop.round.RoundRepository;

import java.util.Optional;

@DataJpaTest
@Rollback(value = false)
public class RoundRepositoryTests {
    @Autowired
    private RoundRepository roundRepository;

    @Test
    public void saveRoundTest() {
        Round round = new Round(1, 2, 1, 2, 5, 3, 5, 3);

        roundRepository.save(round);

        Assertions.assertThat(round.getId()).isGreaterThan(0);
    }

    @Test
    public void getUserTest() {
        Round round = roundRepository.findById(1).orElse(null);

        assert round != null;
        Assertions.assertThat(round.getId()).isEqualTo(1);
    }

    @Test
    public void updateUserTest() {
        Round round = roundRepository.findById(1).orElse(null);

        assert round != null;
        round.setGameId(15);
    }

    @Test
    public void deleteUserTest() {
        Round round = roundRepository.findById(1).orElse(null);

        roundRepository.delete(round);

        Round roundCheck = null;
        Optional<Round> roundOptional = roundRepository.findById(1);

        if (roundOptional.isPresent()) {
            roundCheck = roundOptional.get();
        }

        Assertions.assertThat(roundCheck).isNull();
    }
}
