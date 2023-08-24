package pl.umcs.workshop;

import jakarta.servlet.http.Cookie;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import pl.umcs.workshop.game.Game;
import pl.umcs.workshop.game.GameRepository;
import pl.umcs.workshop.round.Round;
import pl.umcs.workshop.round.RoundRepository;
import pl.umcs.workshop.round.RoundResult;
import pl.umcs.workshop.round.RoundService;
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

    private Game game;

    private Round round;

    private String cookie;

    @BeforeEach
    public void setup() {
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

        round = Round.builder()
                .id(1L)
                .gameId(1L)
                .generation(4)
                .userOneId(1L)
                .userTwoId(6L)
                .userTwoAnswerTime(7)
                .userTwoAnswerTime(8)
                .topic(9L)
                .imageSelected(9L)
                .build();

        cookie = JWTCookieHandler.createToken(1L, 1L);
    }

    @Test
    public void givenUserId_whenGetNextRound_thenReturnRoundObject() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.of(User.builder()
                .id(1L)
                .gameId(1L)
                .score(11)
                .generation(3)
                .lastSeen(LocalDateTime.of(2023, 4, 13, 16, 53))
                .cookie(cookie)
                .build()));
        given(gameRepository.findById(1L)).willReturn(Optional.of(game));
        given(roundRepository.getNextRound(1L, 1L, 4)).willReturn(round);

        // when
        Round nextRound = roundService.getNextRound(1L);

        // then
        Assertions.assertThat(nextRound).isNotNull();
        Assertions.assertThat(nextRound.getGeneration()).isEqualTo(4);
        Assertions.assertThat(nextRound.getGame().getId()).isEqualTo(1);
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
        given(userRepository.findById(1L)).willReturn(Optional.of(User.builder()
                .id(1L)
                .gameId(2L)
                .score(11)
                .generation(3)
                .lastSeen(LocalDateTime.of(2023, 4, 13, 16, 53))
                .cookie(cookie)
                .build()));
        given(gameRepository.findById(2L)).willReturn(Optional.empty());

        // then (with when)
        Assertions.assertThatThrownBy(() -> roundService.getNextRound(1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Game not found");
    }

    @Test
    public void givenUserId_whenGetNextRoundForEndedGame_thenThrowGameHasEnded() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.of(User.builder()
                .id(1L)
                .gameId(2L)
                .score(11)
                .generation(3)
                .lastSeen(LocalDateTime.of(2023, 4, 13, 16, 53))
                .cookie(cookie)
                .build()));
        given(gameRepository.findById(2L)).willReturn(Optional.of(Game.builder()
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
                .createDateTime(LocalDateTime.of(2023, 4, 13, 16, 53))
                .endDateTime(LocalDateTime.now())
                .build()));

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
                .gameId(1L)
                .generation(3)
                .userOneId(5L)
                .userTwoId(6L)
                .userTwoAnswerTime(7)
                .userTwoAnswerTime(8)
                .topic(7L)
                .imageSelected(11L)
                .build();

        // when
        boolean value = roundService.isImageCorrect(round);

        // then
        Assertions.assertThat(value).isEqualTo(false);
    }
}