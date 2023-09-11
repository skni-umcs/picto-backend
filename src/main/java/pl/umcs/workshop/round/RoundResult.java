package pl.umcs.workshop.round;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RoundResult {
  private Result result;
  private int points;

  public enum Result {
    CORRECT,
    WRONG,
    TIME_END
  }
}
