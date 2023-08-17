package pl.umcs.workshop;


import org.junit.Test;
import org.assertj.core.api.Assertions;
import pl.umcs.workshop.round.RoundResult;

public class RoundResultTests {
    @Test
    public void saveCorrectRoundResultTest() {
        RoundResult roundResult = new RoundResult(RoundResult.Result.CORRECT, 1);

        Assertions.assertThat(roundResult.getResult()).isEqualTo(RoundResult.Result.CORRECT);
        Assertions.assertThat(roundResult.getPoints()).isEqualTo(1);
    }

    @Test
    public void saveWrongRoundResultTest() {
        RoundResult roundResult = new RoundResult(RoundResult.Result.WRONG, -1);

        Assertions.assertThat(roundResult.getResult()).isEqualTo(RoundResult.Result.WRONG);
        Assertions.assertThat(roundResult.getPoints()).isEqualTo(-1);
    }

    @Test
    public void saveTimeEndRoundResultTest() {
        RoundResult roundResult = new RoundResult(RoundResult.Result.TIME_END, 0);

        Assertions.assertThat(roundResult.getResult()).isEqualTo(RoundResult.Result.TIME_END);
        Assertions.assertThat(roundResult.getPoints()).isEqualTo(0);
    }
}
