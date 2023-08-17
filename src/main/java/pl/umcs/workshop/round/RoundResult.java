package pl.umcs.workshop.round;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RoundResult {
    public enum Result {
        CORRECT,
        WRONG,
        TIME_END;
    }

    private Result result;
    private int points;
}
