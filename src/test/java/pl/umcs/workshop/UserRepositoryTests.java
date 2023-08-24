package pl.umcs.workshop;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import pl.umcs.workshop.game.Game;
import pl.umcs.workshop.game.GameRepository;
import pl.umcs.workshop.topology.Topology;
import pl.umcs.workshop.topology.TopologyRepository;
import pl.umcs.workshop.user.User;
import pl.umcs.workshop.user.UserRepository;
import pl.umcs.workshop.utils.JWTCookieHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@Rollback(value = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopologyRepository topologyRepository;

    @Autowired
    private GameRepository gameRepository;

    private static Game game;
    private static Game gameTwo;
    private static Topology topology;

    @BeforeAll
    public static void setup() {
        topology = Topology.builder()
                .id(1L)
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

        gameTwo = Game.builder()
                .id(2L)
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
    }

    public void saveSetup() {
        topologyRepository.save(topology);

        gameRepository.save(game);
        gameRepository.save(gameTwo);
    }

    @Test
    @Order(value = 1)
    public void saveUserTest() {
        saveSetup();

        User user = User.builder()
                .game(game)
                .score(6)
                .lastSeen(LocalDateTime.now())
                .cookie(JWTCookieHandler.createToken(1L, 1L))
                .build();

        userRepository.save(user);

        Assertions.assertThat(user.getId()).isEqualTo(1L);
    }

    @Test
    @Order(value = 2)
    public void getUserTest() {
        User user = userRepository.findById(1L).orElse(null);

        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getId()).isEqualTo(1L);
    }

    @Test
    @Order(value = 3)
    public void getListOfAllUsersTest() {
        User user = User.builder()
                .game(gameTwo)
                .score(11)
                .lastSeen(LocalDateTime.now())
                .cookie(JWTCookieHandler.createToken(2L, 2L))
                .build();

        userRepository.save(user);
        List<User> users = userRepository.findAll();

        Assertions.assertThat(users.size()).isEqualTo(2);
    }

    @Test
    @Order(value = 4)
    public void getListOfAllUsersByGameIdTest() {
        User user = User.builder()
                .id(3L)
                .game(game)
                .score(17)
                .lastSeen(LocalDateTime.now())
                .cookie(JWTCookieHandler.createToken(1L, 3L))
                .build();

        userRepository.save(user);
        List<User> users = userRepository.findAllByGameId(1L);

        Assertions.assertThat(users.size()).isEqualTo(2);
    }

    @Test
    @Order(value = 5)
    public void updateUserTest() {
        User user = userRepository.findById(3L).orElse(null);

        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getScore()).isEqualTo(17);

        user.setScore(user.getScore() + -7);

        User savedUser = userRepository.save(user);

        Assertions.assertThat(user.getScore()).isEqualTo(10);
        Assertions.assertThat(user.getId()).isEqualTo(savedUser.getId());

        Assertions.assertThat(savedUser.getScore()).isEqualTo(10);
    }

    @Test
    @Order(value = 6)
    public void deleteUserTest() {
        User user = userRepository.findById(1L).orElse(null);

        Assertions.assertThat(user).isNotNull();
        userRepository.delete(user);

        User userCheck = null;
        Optional<User> userOptional = userRepository.findById(1L);

        if (userOptional.isPresent()) {
            userCheck = userOptional.get();
        }

        Assertions.assertThat(userCheck).isNull();
    }
}
