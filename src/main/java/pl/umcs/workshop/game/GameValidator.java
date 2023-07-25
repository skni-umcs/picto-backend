package pl.umcs.workshop.game;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class GameValidator {
    /* Validator to check if data is semantically correct
    (e.g. if end date is after start date and so on) */
    public static boolean validate(Game game) {
//        if false
//        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);

        return true;
    }
}
