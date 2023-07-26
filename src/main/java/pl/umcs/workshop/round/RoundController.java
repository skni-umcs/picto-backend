package pl.umcs.workshop.round;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RoundController {
    RoundService roundService;

    // TODO change to manual wiring
    @Autowired
    public RoundController(RoundService roundService) {
        this.roundService = roundService;
    }
}
