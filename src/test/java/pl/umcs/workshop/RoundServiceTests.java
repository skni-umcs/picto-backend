package pl.umcs.workshop;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.umcs.workshop.game.Game;
import pl.umcs.workshop.game.GameRepository;
import pl.umcs.workshop.round.Round;
import pl.umcs.workshop.round.RoundRepository;
import pl.umcs.workshop.round.RoundService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RoundServiceTests {
    @Mock
    private GameRepository gameRepository;

    @Mock
    private RoundRepository roundRepository;

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
                .generation(3)
                .userOneId(5)
                .userTwoId(6)
                .userTwoAnswerTime(7)
                .userTwoAnswerTime(8)
                .topic(9)
                .imageSelected(9)
                .build();
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
        int pointsGotten = roundService.getRoundResult(round.getId());

        // then
        Assertions.assertThat(pointsGotten).isEqualTo(1);
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
        int pointsGotten = roundService.getRoundResult(round.getId());

        // then
        Assertions.assertThat(pointsGotten).isEqualTo(-1);
    }
}