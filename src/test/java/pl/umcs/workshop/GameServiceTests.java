package pl.umcs.workshop;

import jakarta.servlet.http.Cookie;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.umcs.workshop.game.Game;
import pl.umcs.workshop.game.GameRepository;
import pl.umcs.workshop.game.GameService;
import pl.umcs.workshop.user.User;
import pl.umcs.workshop.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class GameServiceTests {
    @Mock
    private GameRepository gameRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GameService gameService;

    private Game game;

    @BeforeEach
    public void setup() {
        game = Game.builder()
                .id(1)
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
    }

    @Test
    public void givenGameObject_whenCreateGame_thenReturnGameObject() {
        // given
        given(gameRepository.save(game)).willReturn(game);

        // when
        Game createdGame = gameService.createGame(game);

        // then
        Assertions.assertThat(createdGame).isNotNull();
        Assertions.assertThat(createdGame.getId()).isEqualTo(1);
    }

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

    // TODO: almost done; how to return the user object created inside the method
//    @Test
//    public void givenGameId_whenJoinGame_thenReturnUserObject() {
//        given(gameRepository.findById(1)).willReturn(Optional.of(game));
//        // TODO: this cast doesn't work
//        given(userRepository.save(Mockito.any(User.class))).willReturn((User) returnsFirstArg());
//
//        // when
//        User savedUser = gameService.joinGame(1);
//
//        // then
//        Assertions.assertThat(savedUser).isNotNull();
//        Assertions.assertThat(savedUser.getId()).isEqualTo(1);
//    }

    @Test
    public void givenGameId_whenJoinGame_thenReturnUserObject() {
        List<User> usersList = List.of(new User[]{
                User.builder()
                        .id(1)
                        .gameId(1)
                        .score(11)
                        .generation(3)
                        .lastSeen(LocalDateTime.of(2023, 4, 13, 16, 53))
                        .cookie(new Cookie("cookieOne", "valueOfCookieOne"))
                        .build(),
                User.builder()
                        .id(2)
                        .gameId(1)
                        .score(13)
                        .generation(1)
                        .lastSeen(LocalDateTime.of(2023, 4, 13, 17, 6))
                        .cookie(new Cookie("cookieTwo", "valueOfCookieTwo"))
                        .build(),
                User.builder()
                        .id(3)
                        .gameId(1)
                        .score(7)
                        .generation(1)
                        .lastSeen(LocalDateTime.of(2023, 4, 13, 16, 21))
                        .cookie(new Cookie("cookieThree", "valueOfCookieThree"))
                        .build()
        });

        given(gameRepository.findById(1)).willReturn(Optional.of(game));
        given(gameRepository.save(game)).willReturn(game);
        given(userRepository.findAllByGameId(1)).willReturn(usersList);
        // TODO: fix this to return users will null cookies (modified by endGame method)
//        given(userRepository.saveAll(usersList).willReturn();

        // when
        Game endedGame = gameService.endGame(1);
        List<User> users = userRepository.findAllByGameId(1);

        // then
        Assertions.assertThat(endedGame).isNotNull();
        Assertions.assertThat(endedGame.getId()).isEqualTo(1);
        Assertions.assertThat(endedGame.getEndDateTime()).isNotNull();

        Assertions.assertThat(users.size()).isEqualTo(3);
        // TODO: enable when former TODO is done
//        Assertions.assertThat(users.get(0).getCookie()).isNull();
    }
}
