package pl.umcs.workshop.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.UUID;

public class JWTCookieHandler {
    // TODO: get this from env
    private static final String secret = "test";
    static Algorithm algorithm = Algorithm.HMAC256(secret);
    static JWTVerifier verifier = JWT.require(algorithm)
            .withIssuer("picto")
            .build();

    public static String createToken(Long gameId, Long userId) {
        return JWT.create()
                .withIssuer("picto")
                .withSubject("User cookie")
                .withClaim("gameId", gameId)
                .withClaim("userId", userId)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 43200000L))
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date(System.currentTimeMillis()))
                .sign(algorithm);
    }

    public static DecodedJWT verifyToken(String token) {
        try {
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException("Could not verify token");
        }
    }

    public static Long getUserId(String token) {
        DecodedJWT verifiedToken = verifyToken(token);

        return verifiedToken.getClaim("userId").asLong();
    }

}
