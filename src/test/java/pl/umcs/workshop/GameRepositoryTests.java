package pl.umcs.workshop;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import pl.umcs.workshop.game.Game;
import pl.umcs.workshop.game.GameRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@DataJpaTest
@Rollback(value = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameRepositoryTests {
    @Autowired
    private GameRepository gameRepository;

    @Test
    @Order(value = 1)
    public void saveGameTest() {
        Game game = Game.builder()
                .userOneNumberOfImages(4)
                .userTwoNumberOfImages(4)
                .userOneTime(5)
                .userTwoTime(3)
                .symbolGroupsAmount(3)
                .symbolsInGroupAmount(4)
                .correctAnswerPoints(1)
                .wrongAnswerPoints(-1)
                .topologyId(1)
                .createDateTime(LocalDateTime.now())
                .build();

        gameRepository.save(game);

        Assertions.assertThat(game.getId()).isGreaterThan(0);
    }

    @Test
    @Order(value = 2)
    public void getGameTest() {
        Game game = gameRepository.findById(1).orElse(null);

        assert game != null;
        Assertions.assertThat(game.getId()).isEqualTo(1);
        Assertions.assertThat(game.getCreateDateTime()).isNotNull();
    }

    @Test
    @Order(value = 3)
    public void updateGameTest() {
        Game game = gameRepository.findById(1).orElse(null);

        assert game != null;
        game.setUserOneNumberOfImages(4);
    }

    @Test
    @Order(value = 4)
    public void deleteGameTest() {
        Game game = gameRepository.findById(1).orElse(null);

        assert game != null;
        gameRepository.delete(game);

        Game gameCheck = null;
        Optional<Game> gameOptional = gameRepository.findById(1);

        if (gameOptional.isPresent()) {
            gameCheck = gameOptional.get();
        }

        Assertions.assertThat(gameCheck).isNull();
    }
}
