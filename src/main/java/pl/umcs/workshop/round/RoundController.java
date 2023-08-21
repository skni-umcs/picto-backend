package pl.umcs.workshop.round;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.umcs.workshop.image.Image;
import pl.umcs.workshop.user.UserInfo;

import java.util.List;

@RestController
@RequestMapping("round/")
public class RoundController {
    RoundService roundService;

    @Autowired
    public RoundController(RoundService roundService) {
        this.roundService = roundService;
    }

    @GetMapping("next/{userId}")
    public Round getNextRound(@PathVariable Long userId) {
        return roundService.getNextRound(userId);
    }

    @GetMapping("{roundId}/speaker")
    public List<Image> getRoundSpeakerInfo(@PathVariable Long roundId) {
        return roundService.getSpeakerImages(roundId);
    }

    // TODO
    @GetMapping("{roundId}/listener")
    public Round getRoundListenerInfo(@PathVariable Long roundId) {
        return roundService.getRoundListenerInfo(roundId);
    }

    // TODO
    @PostMapping("{roundId}/speaker")
    public Round saveRoundSpeakerInfo(@RequestBody UserInfo userInfo) {
        return roundService.saveRoundSpeakerInfo(userInfo);
    }

    // TODO
    @PostMapping("{roundId}/listener")
    public Round saveRoundListenerInfo(@RequestBody UserInfo userInfo) {
        return roundService.saveRoundListenerInfo(userInfo);
    }

    @GetMapping("{roundId}/result")
    public RoundResult getRoundResult(@PathVariable Long roundId) {
        return roundService.getRoundResult(roundId);
    }
}
