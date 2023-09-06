package pl.umcs.workshop;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.umcs.workshop.game.Game;
import pl.umcs.workshop.game.GameRepository;
import pl.umcs.workshop.game.GameService;
import pl.umcs.workshop.topology.Topology;
import pl.umcs.workshop.topology.TopologyRepository;
import pl.umcs.workshop.user.User;
import pl.umcs.workshop.user.UserRepository;
import pl.umcs.workshop.utils.JwtCookieHandler;

@ExtendWith(MockitoExtension.class)
public class GameServiceTests {
    @Mock
    private GameRepository gameRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TopologyRepository topologyRepository;

    @InjectMocks
    private GameService gameService;

    private Game game;
    private Topology topology;

    @BeforeEach
    public void setup() {
        topology = Topology.builder().build();

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
    }

//    @Test
//    public void givenGameObject_whenCreateGame_thenReturnGameObject() {
//        // given
//        given(gameRepository.save(game)).willReturn(game);
//        given(topologyRepository.save(any(Topology.class))).willReturn();
//
//        // when
//        Game createdGame = gameService.createGame(game);
//
//        // then
//        Assertions.assertThat(createdGame).isNotNull();
//        Assertions.assertThat(createdGame.getId()).isEqualTo(1);
//    }

    // TODO when beginGame works
//    @Test
//    public void givenGameId_whenBeginGame_thenReturnListOfIntegers() {
//        // given
//        given(gameRepository.save(game)).willReturn(game);
//
//        // when
//        Game createdGame = gameService.createGame(game);
//
//        // then
//        Assertions.assertThat(createdGame).isNotNull();
//        Assertions.assertThat(createdGame.getId()).isEqualTo(1);
//    }

    @Test
    public void givenGameId_whenJoinGame_thenReturnUserObject() {
        given(gameRepository.findById(1L)).willReturn(Optional.of(game));
        given(userRepository.save(any(User.class))).willReturn(User.builder()
                .id(1L)
                .game(game)
                .score(0)
                .generation(0)
                .lastSeen(LocalDateTime.of(2023, 4, 13, 16, 53))
                .cookie(JwtCookieHandler.createToken(1L, 1L))
                .build());

        // when
        User savedUser = gameService.joinGame(1L);

        // then
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isEqualTo(1);
    }

    @Test
    public void givenGameIdAndUserId_whenJoinGameAsUser_thenReturnUserObject() {
        User user = User.builder()
                .id(1L)
                .game(game)
                .score(11)
                .generation(3)
                .lastSeen(LocalDateTime.of(2023, 4, 13, 16, 53))
                .cookie(JwtCookieHandler.createToken(1L, 1L))
                .build();

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(gameRepository.findById(1L)).willReturn(Optional.of(game));
        given(userRepository.save(user)).willReturn(user);

        // when
        User joinedUser = gameService.joinGameAsUser(1L, 1L);

        // then
        Assertions.assertThat(joinedUser).isNotNull();
        Assertions.assertThat(joinedUser.getId()).isEqualTo(1);
    }

    @Test
    public void givenGameId_whenEndGame_thenReturnUserObject() throws IOException {
        given(gameRepository.findById(1L)).willReturn(Optional.of(game));
        given(gameRepository.save(game)).willReturn(game);

        // when
        Game endedGame = gameService.endGame(1L);

        // then
        Assertions.assertThat(endedGame).isNotNull();
        Assertions.assertThat(endedGame.getId()).isEqualTo(1);
        Assertions.assertThat(endedGame.getEndDateTime()).isNotNull();
    }

    @Test
    public void givenGameId_whenDeleteUserCookies_thenReturnListOfUsers() {
        List<User> users = List.of(new User[]{
                User.builder()
                        .id(1L)
                        .game(game)
                        .score(11)
                        .generation(3)
                        .lastSeen(LocalDateTime.of(2023, 4, 13, 16, 53))
                        .cookie(JwtCookieHandler.createToken(1L, 1L))
                        .build(),
                User.builder()
                        .id(2L)
                        .game(game)
                        .score(13)
                        .generation(1)
                        .lastSeen(LocalDateTime.of(2023, 4, 13, 17, 6))
                        .cookie(JwtCookieHandler.createToken(2L, 1L))
                        .build(),
                User.builder()
                        .id(3L)
                        .game(game)
                        .score(7)
                        .generation(1)
                        .lastSeen(LocalDateTime.of(2023, 4, 13, 16, 21))
                        .cookie(JwtCookieHandler.createToken(3L, 1L))
                        .build()
        });

        given(userRepository.findAllByGameId(1L)).willReturn(users);
        given(userRepository.saveAll(users)).willReturn(users);

        // when
        List<User> returnedUsers = gameService.deleteUserCookies(1L);

        // then
        Assertions.assertThat(returnedUsers.size()).isEqualTo(3);
    }
}
