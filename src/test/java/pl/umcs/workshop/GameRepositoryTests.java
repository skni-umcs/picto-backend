package pl.umcs.workshop;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import pl.umcs.workshop.game.Game;
import pl.umcs.workshop.game.GameRepository;

import java.util.Optional;

@DataJpaTest
@Rollback(value = false)
public class GameRepositoryTests {
    @Autowired
    private GameRepository gameRepository;

    @Test
    public void saveGameTest() {
        Game game = new Game(2, 3, 10, 5, 3, 3, 1, -1, 1);

        gameRepository.save(game);

        Assertions.assertThat(game.getId()).isGreaterThan(0);
    }

    @Test
    public void getGameTest() {
        Game game = gameRepository.findById(1).orElse(null);

        assert game != null;
        Assertions.assertThat(game.getId()).isEqualTo(1);
    }

    @Test
    public void updateGameTest() {
        Game game = gameRepository.findById(1).orElse(null);

        assert game != null;
        game.setUserOneNumberOfImages(4);
    }

    @Test
    public void deleteGameTest() {
        Game game = gameRepository.findById(1).orElse(null);

        gameRepository.delete(game);

        Game gameCheck = null;
        Optional<Game> gameOptional = gameRepository.findById(1);

        if (gameOptional.isPresent()) {
            gameCheck = gameOptional.get();
        }

        Assertions.assertThat(gameCheck).isNull();
    }
}
