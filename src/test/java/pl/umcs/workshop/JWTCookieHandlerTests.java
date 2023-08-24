package pl.umcs.workshop;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.umcs.workshop.utils.JWTCookieHandler;

public class JWTCookieHandlerTests {
    @Test
    public void createTokenTest() {
        String token = JWTCookieHandler.createToken(1L, 1L);

        Assertions.assertThat(token).isNotNull();
    }

    @Test
    public void verifyTokenTest() {
        String token = JWTCookieHandler.createToken(1L, 1L);
        DecodedJWT decodedToken = JWTCookieHandler.verifyToken(token);

        Assertions.assertThat(decodedToken.getClaim("userId").asLong()).isEqualTo(1L);
    }

    @Test
    public void getUserIdTest() {
        String token = JWTCookieHandler.createToken(1L, 1L);
        DecodedJWT decodedToken = JWTCookieHandler.verifyToken(token);

        Long userId = JWTCookieHandler.getUserId(decodedToken);

        Assertions.assertThat(userId).isEqualTo(1L);
    }
}
