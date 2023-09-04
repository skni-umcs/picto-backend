package pl.umcs.workshop;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import pl.umcs.workshop.game.Game;
import pl.umcs.workshop.game.GameRepository;
import pl.umcs.workshop.topology.Topology;
import pl.umcs.workshop.topology.TopologyRepository;

@DataJpaTest
@Rollback(value = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class GameRepositoryTests {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private TopologyRepository topologyRepository;

    @Test
    @Order(value = 1)
    public void saveGameTest() {
        Topology topology = Topology.builder()
                .maxVertexDegree(5)
                .probabilityOfEdgeRedrawing(0.55)
                .build();
        topologyRepository.save(topology);

        Game game = Game.builder()
                .userOneNumberOfImages(4)
                .userTwoNumberOfImages(4)
                .userOneTime(5)
                .userTwoTime(3)
                .symbolGroupsAmount(3)
                .symbolsInGroupAmount(4)
                .correctAnswerPoints(1)
                .wrongAnswerPoints(-1)
                .topology(topology)
                .createDateTime(LocalDateTime.now())
                .build();
        Game savedGame = gameRepository.save(game);

        Assertions.assertThat(savedGame.getId()).isEqualTo(1L);
    }

    @Test
    @Order(value = 2)
    public void getGameTest() {
        Game game = gameRepository.findById(1L).orElse(null);

        Assertions.assertThat(game).isNotNull();
        Assertions.assertThat(game.getId()).isEqualTo(1);
        Assertions.assertThat(game.getCreateDateTime()).isNotNull();
    }

    @Test
    @Order(value = 3)
    public void getListOfAllGamesTest() {
        Topology topology = Topology.builder()
                .maxVertexDegree(3)
                .probabilityOfEdgeRedrawing(0.16)
                .build();
        topologyRepository.save(topology);

        Game game = Game.builder()
                .userOneNumberOfImages(3)
                .userTwoNumberOfImages(5)
                .userOneTime(7)
                .userTwoTime(4)
                .symbolGroupsAmount(4)
                .symbolsInGroupAmount(2)
                .correctAnswerPoints(2)
                .wrongAnswerPoints(-1)
                .topology(topology)
                .createDateTime(LocalDateTime.now())
                .build();
        gameRepository.save(game);

        List<Game> games = gameRepository.findAll();

        Assertions.assertThat(games.size()).isEqualTo(2);
    }

    @Test
    @Order(value = 4)
    public void updateGameTest() {
        Game game = gameRepository.findById(1L).orElse(null);

        Assertions.assertThat(game).isNotNull();
        game.setUserOneNumberOfImages(4);

        Game savedGame = gameRepository.save(game);

        Assertions.assertThat(savedGame.getUserOneNumberOfImages()).isEqualTo(4);
        Assertions.assertThat(game.getId()).isEqualTo(savedGame.getId());
    }

    @Test
    @Order(value = 5)
    public void deleteGameTest() {
        Game game = gameRepository.findById(1L).orElse(null);

        Assertions.assertThat(game).isNotNull();
        gameRepository.delete(game);

        Game gameCheck = null;
        Optional<Game> gameOptional = gameRepository.findById(1L);

        if (gameOptional.isPresent()) {
            gameCheck = gameOptional.get();
        }

        Assertions.assertThat(gameCheck).isNull();
    }
}
