package pl.umcs.workshop;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.umcs.workshop.utils.JwtCookieHandler;

public class JwtCookieHandlerTests {
  @Test
  public void createTokenTest() {
    String token = JwtCookieHandler.createToken(1L, 1L);

    Assertions.assertThat(token).isNotNull();
  }

  @Test
  public void verifyTokenTestValid() {
    String token = JwtCookieHandler.createToken(1L, 1L);
    DecodedJWT decodedToken = JwtCookieHandler.verifyToken(token);
    Long userId = decodedToken.getClaim("userId").asLong();

    Assertions.assertThat(userId).isEqualTo(1L);
  }

  @Test
  public void verifyTokenTestInvalid() {
    String invalidToken =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

    Assertions.assertThatThrownBy(() -> JwtCookieHandler.verifyToken(invalidToken))
        .isInstanceOf(JWTVerificationException.class)
        .hasMessageContaining("Could not verify token");
  }

  @Test
  public void getUserIdTestValidToken() {
    String token = JwtCookieHandler.createToken(1L, 1L);
    Long userId = JwtCookieHandler.getUserId(token);

    Assertions.assertThat(userId).isEqualTo(1L);
  }

  @Test
  public void getUserIdTestInvalidToken() {
    String invalidToken =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

    Assertions.assertThatThrownBy(() -> JwtCookieHandler.getUserId(invalidToken))
        .isInstanceOf(JWTVerificationException.class)
        .hasMessageContaining("Could not verify token");
  }
}
