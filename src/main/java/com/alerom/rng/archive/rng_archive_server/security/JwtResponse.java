package com.alerom.rng.archive.rng_archive_server.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Data Transfer Object (DTO) for JWT authentication responses.
 * This class encapsulates the JWT token along with essential user information.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public class JwtResponse {

    /**
     * The JSON Web Token (JWT) issued to the authenticated user.
     */
    private String token;

    /**
     * The unique identifier of the authenticated user.
     */
    private Long id;

    /**
     * The username of the authenticated user.
     */
    private String userName;

    /**
     * The rol of the authenticated user.
     */
    private boolean isAdmin;
}