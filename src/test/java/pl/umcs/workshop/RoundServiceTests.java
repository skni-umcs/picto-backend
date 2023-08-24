package pl.umcs.workshop;

import jakarta.servlet.http.Cookie;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import pl.umcs.workshop.game.Game;
import pl.umcs.workshop.game.GameRepository;
import pl.umcs.workshop.image.Image;
import pl.umcs.workshop.round.Round;
import pl.umcs.workshop.round.RoundRepository;
import pl.umcs.workshop.round.RoundResult;
import pl.umcs.workshop.round.RoundService;
import pl.umcs.workshop.topology.Topology;
import pl.umcs.workshop.user.User;
import pl.umcs.workshop.user.UserRepository;
import pl.umcs.workshop.utils.JWTCookieHandler;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class RoundServiceTests {
    @Mock
    private GameRepository gameRepository;

    @Mock
    private RoundRepository roundRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RoundService roundService;

    private static Game game;
    private static Game gameInvalid;
    private static Game gameEnded;
    private static Topology topology;
    private static Round round;
    private static User userOne;
    private static User userTwo;
    private static Image topic;
    private static Image imageSelected;

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

        gameInvalid = Game.builder()
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

        gameEnded = Game.builder()
                .id(3L)
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
                .endDateTime(LocalDateTime.now())
                .build();

        userOne = User.builder()
                .id(1L)
                .game(game)
                .score(6)
                .generation(3)
                .lastSeen(LocalDateTime.now())
                .cookie(JWTCookieHandler.createToken(1L, 1L))
                .build();

        userTwo = User.builder()
                .id(2L)
                .game(game)
                .score(8)
                .generation(3)
                .lastSeen(LocalDateTime.now())
                .cookie(JWTCookieHandler.createToken(2L, 2L))
                .build();

        topic = Image.builder()
                .id(1L)
                .path("flower.jpg")
                .build();

        imageSelected = Image.builder()
                .id(2L)
                .path("car.jpg")
                .build();

        round = Round.builder()
                .id(1L)
                .game(game)
                .generation(4)
                .userOne(userOne)
                .userTwo(userTwo)
                .userTwoAnswerTime(7)
                .userTwoAnswerTime(8)
                .topic(topic)
                .imageSelected(topic)
                .build();
    }

    @Test
    public void givenUserId_whenGetNextRound_thenReturnRoundObject() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.of(userOne));
        given(gameRepository.findById(1L)).willReturn(Optional.of(game));
        given(roundRepository.getNextRound(1L, 1L, 4)).willReturn(round);

        // when
        Round nextRound = roundService.getNextRound(1L);

        // then
        Assertions.assertThat(nextRound).isNotNull();
        Assertions.assertThat(nextRound.getGeneration()).isEqualTo(4);
        Assertions.assertThat(nextRound.getGame().getId()).isEqualTo(1L);
    }

    @Test
    public void givenUserId_whenGetNextRoundForInvalidUser_thenThrowUserNotFound() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        // then (with when)
        Assertions.assertThatThrownBy(() -> roundService.getNextRound(1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    public void givenUserId_whenGetNextRoundForInvalidGame_thenThrowGameNotFound() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.of(userOne));
        given(gameRepository.findById(1L)).willReturn(Optional.empty());

        // then (with when)
        Assertions.assertThatThrownBy(() -> roundService.getNextRound(1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Game not found");
    }

    @Test
    public void givenUserId_whenGetNextRoundForEndedGame_thenThrowGameHasEnded() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.of(userOne));
        given(gameRepository.findById(1L)).willReturn(Optional.of(gameEnded));

        // then (with when)
        Assertions.assertThatThrownBy(() -> roundService.getNextRound(1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Game has ended");
    }

    // Save speaker

    // Save listener

    @Test
    public void givenRoundId_whenGetRoundResult_thenReturnPointsGottenOnCorrect() {
        // given
        given(gameRepository.findById(1L)).willReturn(Optional.of(game));
        given(roundRepository.findById(1L)).willReturn(Optional.of(round));

        // when
        RoundResult roundResult = roundService.getRoundResult(round.getId());

        // then
        Assertions.assertThat(roundResult.getResult()).isEqualTo(RoundResult.Result.CORRECT);
        Assertions.assertThat(roundResult.getPoints()).isEqualTo(1);
    }

    @Test
    public void givenRoundId_whenGetRoundResult_thenReturnPointsGottenOnWrong() {
        // given
        given(gameRepository.findById(1L)).willReturn(Optional.empty());
        given(roundRepository.findById(1L)).willReturn(Optional.of(round));

        // then
        Assertions.assertThatThrownBy(() -> roundService.getRoundResult(1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Game not found");
    }

    @Test
    public void givenInvalidRoundId_whenGetRoundResult_thenThrowRoundNotFound() {
        // given
        given(roundRepository.findById(1L)).willReturn(Optional.empty());

        // then
        Assertions.assertThatThrownBy(() -> roundService.getRoundResult(1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Round not found");
    }

    @Test
    public void givenRoundIdForInvalidGame_whenGetRoundResult_thenThrowGameNotFound() {
        // given
        given(roundRepository.findById(1L)).willReturn(Optional.empty());

        // then
        Assertions.assertThatThrownBy(() -> roundService.getRoundResult(1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Round not found");
    }

    @Test
    public void givenRoundObject_whenIsImageCorrect_thenReturnBooleanOnCorrect() {
        // when
        boolean value = roundService.isImageCorrect(round);

        // then
        Assertions.assertThat(value).isEqualTo(true);
    }

    @Test
    public void givenRoundObject_whenIsImageCorrect_thenReturnBooleanOnWrong() {
        // given
        Round round = Round.builder()
                .id(2L)
                .game(game)
                .generation(3)
                .userOne(userOne)
                .userTwo(userTwo)
                .userTwoAnswerTime(7)
                .userTwoAnswerTime(8)
                .topic(topic)
                .imageSelected(imageSelected)
                .build();

        // when
        boolean value = roundService.isImageCorrect(round);

        // then
        Assertions.assertThat(value).isEqualTo(false);
    }
}