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

        round = Round.builder()
                .id(1)
                .gameId(1)
                .generation(4)
                .userOneId(1)
                .userTwoId(6)
                .userTwoAnswerTime(7)
                .userTwoAnswerTime(8)
                .topic(9)
                .imageSelected(9)
                .build();
    }

    @Test
    public void givenUserId_whenGetNextRound_thenReturnRoundObject() {
        // given
        given(userRepository.findById(1)).willReturn(Optional.of(User.builder()
                .id(1)
                .gameId(1)
                .score(11)
                .generation(3)
                .lastSeen(LocalDateTime.of(2023, 4, 13, 16, 53))
                .cookie(new Cookie("cookieOne", "valueOfCookieOne"))
                .build()));
        given(gameRepository.findById(1)).willReturn(Optional.of(game));
        given(roundRepository.getNextRound(1, 1, 4)).willReturn(round);

        // when
        Round nextRound = roundService.getNextRound(1);

        // then
        Assertions.assertThat(nextRound).isNotNull();
        Assertions.assertThat(nextRound.getGeneration()).isEqualTo(4);
        Assertions.assertThat(nextRound.getGameId()).isEqualTo(1);
    }

    @Test
    public void givenUserId_whenGetNextRoundForInvalidUser_thenThrowUserNotFound() {
        // given
        given(userRepository.findById(1)).willReturn(Optional.empty());

        // then (with when)
        Assertions.assertThatThrownBy(() -> roundService.getNextRound(1))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    public void givenUserId_whenGetNextRoundForInvalidGame_thenThrowGameNotFound() {
        // given
        given(userRepository.findById(1)).willReturn(Optional.of(User.builder()
                .id(1)
                .gameId(2)
                .score(11)
                .generation(3)
                .lastSeen(LocalDateTime.of(2023, 4, 13, 16, 53))
                .cookie(new Cookie("cookieOne", "valueOfCookieOne"))
                .build()));
        given(gameRepository.findById(2)).willReturn(Optional.empty());

        // then (with when)
        Assertions.assertThatThrownBy(() -> roundService.getNextRound(1))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Game not found");
    }

    @Test
    public void givenUserId_whenGetNextRoundForEndedGame_thenThrowGameHasEnded() {
        // given
        given(userRepository.findById(1)).willReturn(Optional.of(User.builder()
                .id(1)
                .gameId(2)
                .score(11)
                .generation(3)
                .lastSeen(LocalDateTime.of(2023, 4, 13, 16, 53))
                .cookie(new Cookie("cookieOne", "valueOfCookieOne"))
                .build()));
        given(gameRepository.findById(2)).willReturn(Optional.of(Game.builder()
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
                .createDateTime(LocalDateTime.of(2023, 4, 13, 16, 53))
                .endDateTime(LocalDateTime.now())
                .build()));

        // then (with when)
        Assertions.assertThatThrownBy(() -> roundService.getNextRound(1))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Game has ended");
    }

    // Get speaker
//    @Test
//    public void givenRoundId_whenGetRoundSpeakerInfo_thenReturnRoundObject() {
//        // given
//        given(roundRepository.findById(1)).willReturn(Optional.of(round));
//
//        // when
//        Round foundRound = roundService.getRound(round.getId());
//
//        // then
//        Assertions.assertThat(foundRound).isNotNull();
//        Assertions.assertThat(foundRound.getId()).isEqualTo(round.getId());
//    }

    // Get listener
//    @Test
//    public void givenRoundId_whenGetRoundListenerInfo_thenReturnRoundObject() {
//        // given
//        given(roundRepository.findById(1)).willReturn(Optional.of(round));
//
//        // when
//        Round foundRound = roundService.getRound(round.getId());
//
//        // then
//        Assertions.assertThat(foundRound).isNotNull();
//        Assertions.assertThat(foundRound.getId()).isEqualTo(round.getId());
//    }

    // Save speaker

    // Save listener

    @Test
    public void givenRoundId_whenGetRoundResult_thenReturnPointsGottenOnCorrect() {
        // given
        given(gameRepository.findById(1)).willReturn(Optional.of(game));
        given(roundRepository.findById(1)).willReturn(Optional.of(round));

        // when
        RoundResult roundResult = roundService.getRoundResult(round.getId());

        // then
        Assertions.assertThat(roundResult.getResult()).isEqualTo(RoundResult.Result.CORRECT);
        Assertions.assertThat(roundResult.getPoints()).isEqualTo(1);
    }

    @Test
    public void givenRoundId_whenGetRoundResult_thenReturnPointsGottenOnWrong() {
        // given
        Round round = Round.builder()
                .id(2)
                .gameId(1)
                .generation(4)
                .userOneId(5)
                .userTwoId(6)
                .userTwoAnswerTime(7)
                .userTwoAnswerTime(8)
                .topic(10)
                .imageSelected(9)
                .build();

        given(gameRepository.findById(1)).willReturn(Optional.of(game));
        given(roundRepository.findById(2)).willReturn(Optional.of(round));

        // when
        RoundResult roundResult = roundService.getRoundResult(round.getId());

        // then
        Assertions.assertThat(roundResult.getResult()).isEqualTo(RoundResult.Result.WRONG);
        Assertions.assertThat(roundResult.getPoints()).isEqualTo(-1);
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
                .id(2)
                .gameId(1)
                .generation(3)
                .userOneId(5)
                .userTwoId(6)
                .userTwoAnswerTime(7)
                .userTwoAnswerTime(8)
                .topic(7)
                .imageSelected(11)
                .build();

        // when
        boolean value = roundService.isImageCorrect(round);

        // then
        Assertions.assertThat(value).isEqualTo(false);
    }
}