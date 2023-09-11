package pl.umcs.workshop.round;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.umcs.workshop.image.Image;
import pl.umcs.workshop.symbol.Symbol;
import pl.umcs.workshop.user.UserInfo;

@RestController
@RequestMapping("round/")
public class RoundController {
  RoundService roundService;

  @Autowired
  public RoundController(RoundService roundService) {
    this.roundService = roundService;
  }

  @GetMapping("next/{userId}")
  public Round getNextRound(@PathVariable Long userId) throws IOException {
    return roundService.getNextRound(userId);
  }

  @GetMapping("{roundId}/images/{userId}")
  public List<Image> getImages(@PathVariable Long roundId, @PathVariable Long userId) {
    return roundService.getImages(roundId, userId);
  }

  @GetMapping("{roundId}/symbols/{userId}")
  public List<List<Symbol>> getSymbols(@PathVariable Long roundId, @PathVariable Long userId) {
    return roundService.getSymbols(roundId, userId);
  }

  @PostMapping("{roundId}/speaker")
  public Round saveRoundSpeakerInfo(@RequestBody UserInfo userInfo) throws IOException {
    return roundService.saveRoundSpeakerInfo(userInfo);
  }

  @PostMapping("{roundId}/listener")
  public Round saveRoundListenerInfo(@RequestBody UserInfo userInfo) {
    return roundService.saveRoundListenerInfo(userInfo);
  }

  @GetMapping("{roundId}/result/{userId}")
  public RoundResult getRoundResult(@PathVariable Long roundId, @PathVariable Long userId) throws IOException {
    return roundService.getRoundResult(roundId, userId);
  }
}
