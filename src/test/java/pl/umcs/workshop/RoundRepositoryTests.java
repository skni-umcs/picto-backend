package pl.umcs.workshop;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import pl.umcs.workshop.game.Game;
import pl.umcs.workshop.game.GameRepository;
import pl.umcs.workshop.image.Image;
import pl.umcs.workshop.image.ImageRepository;
import pl.umcs.workshop.round.Round;
import pl.umcs.workshop.round.RoundRepository;
import pl.umcs.workshop.topology.Topology;
import pl.umcs.workshop.topology.TopologyRepository;
import pl.umcs.workshop.user.User;
import pl.umcs.workshop.user.UserRepository;
import pl.umcs.workshop.utils.JwtCookieHandler;

@DataJpaTest
@Rollback(value = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class RoundRepositoryTests {
    private static Game game;
    private static Topology topology;
    private static User userOne;
    private static User userTwo;
    private static Image topic;
    private static Image imageSelected;
    private static Image imageSelectedUpdate;
    @Autowired
    private RoundRepository roundRepository;
    @Autowired
    private TopologyRepository topologyRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImageRepository imageRepository;

    @BeforeAll
    public static void setup() {
        topology = Topology.builder()
                .maxVertexDegree(5)
                .probabilityOfEdgeRedrawing(0.55)
                .build();

        game = Game.builder()
                .id(1L)
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

        userOne = User.builder()
                .id(1L)
                .game(game)
                .score(6)
                .lastSeen(LocalDateTime.now())
                .cookie(JwtCookieHandler.createToken(1L, 1L))
                .build();

        userTwo = User.builder()
                .id(2L)
                .game(game)
                .score(8)
                .lastSeen(LocalDateTime.now())
                .cookie(JwtCookieHandler.createToken(2L, 2L))
                .build();

        topic = Image.builder()
                .id(1L)
                .path("flower.jpg")
                .build();

        imageSelected = Image.builder()
                .id(2L)
                .path("car.jpg")
                .build();

        imageSelectedUpdate = Image.builder()
                .id(3L)
                .path("person.jpg")
                .build();
    }

    public void saveSetup() {
        topologyRepository.save(topology);

        gameRepository.save(game);

        userRepository.save(userOne);
        userRepository.save(userTwo);

        imageRepository.save(topic);
        imageRepository.save(imageSelected);
        imageRepository.save(imageSelectedUpdate);
    }

    @Test
    @Order(value = 1)
    public void saveRoundTest() {
        saveSetup();

        Round round = Round.builder()
                .game(game)
                .generation(15)
                .userOne(userOne)
                .userTwo(userTwo)
                .userOneAnswerTime(3.52)
                .userTwoAnswerTime(2.61)
                .topic(topic)
                .imageSelected(imageSelected)
                .build();

        roundRepository.save(round);

        Assertions.assertThat(round.getId()).isEqualTo(1L);
    }

    @Test
    @Order(value = 2)
    public void getRoundTest() {
        Round round = roundRepository.findById(1L).orElse(null);

        Assertions.assertThat(round).isNotNull();
        Assertions.assertThat(round.getId()).isEqualTo(1L);
    }

    @Test
    @Order(value = 3)
    public void getListOfAllRoundsTest() {
        Round round = Round.builder()
                .game(game)
                .generation(4)
                .userOne(userTwo)
                .userTwo(userOne)
                .userOneAnswerTime(5.21)
                .userTwoAnswerTime(1.68)
                .topic(imageSelected)
                .imageSelected(topic)
                .build();

        roundRepository.save(round);

        List<Round> rounds = roundRepository.findAll();

        Assertions.assertThat(rounds.size()).isEqualTo(2);
    }

    @Test
    @Order(value = 4)
    public void updateRoundTest() {
        Round round = roundRepository.findById(1L).orElse(null);

        Assertions.assertThat(round).isNotNull();
        Assertions.assertThat(round.getImageSelected().getId()).isEqualTo(2L);

        round.setImageSelected(imageSelectedUpdate);

        Round savedRound = roundRepository.save(round);

        Assertions.assertThat(savedRound).isNotNull();
        Assertions.assertThat(savedRound.getImageSelected().getId()).isEqualTo(3L);

        Assertions.assertThat(round.getImageSelected().getId()).isEqualTo(3L);
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
