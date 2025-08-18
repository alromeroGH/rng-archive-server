package com.alerom.rng.archive.rng_archive_server.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Utility class for JSON Web Token (JWT) creation, validation, and parsing.
 * This class handles all JWT-related operations using the auth0 library.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@Component
public class JwtUtil {
    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.expiration.time.hours}")
    private int hourExpirationTime;

    /**
     * Generates a new JWT token for an authenticated user.
     * The token includes the user's email, ID, and username, and is signed with a secret key.
     *
     * @param authentication The authentication object containing the user's details.
     * @return A signed JWT token as a String.
     * @throws IllegalArgumentException if there's an error during token creation.
     */
    public String generateToken(Authentication authentication) {
        JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();

        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT.create()
                    .withIssuer("${jwt.issuer}")
                    .withSubject(jwtUserDetails.getUsername())
                    .withClaim("ID", jwtUserDetails.user().getId())
                    .withClaim("Name", jwtUserDetails.user().getUserName())
                    .withExpiresAt(generateDateOfExpiration())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new IllegalArgumentException("Error generating JWT token: " + exception.getMessage(), exception);
        }
    }

    /**
     * Validates an incoming JWT token.
     * It verifies the token's signature, issuer, and expiration time.
     *
     * @param tokenJWT The JWT token string to validate.
     * @return A DecodedJWT object if the token is valid.
     * @throws IllegalArgumentException if the token is invalid or expired.
     */
    public DecodedJWT validateToken(String tokenJWT) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("${jwt.issuer}")
                    .build();
            return verifier.verify(tokenJWT);
        } catch (JWTVerificationException exception) {
            throw new IllegalArgumentException("Invalid or expired JWT token: " + exception.getMessage(), exception);
        }
    }

    /**
     * Extracts the email from a decoded JWT token.
     *
     * @param decodedToken The decoded JWT object.
     * @return The subject (email) of the token.
     */
    public String getEmailOfToken(DecodedJWT decodedToken) {
        return decodedToken.getSubject();
    }

    /**
     * Generates the expiration date for a JWT token.
     *
     * @return An Instant representing the token's expiration time.
     */
    private Instant generateDateOfExpiration() {
        return Instant.now().plus(hourExpirationTime, ChronoUnit.HOURS);
    }
}