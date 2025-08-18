package com.alerom.rng.archive.rng_archive_server.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for user login requests.
 * It encapsulates the user's credentials for authentication.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@Getter
@Setter
public class LoginRequest {

    /**
     * The email address of the user. It must be a valid email format and cannot be null or blank.
     */
    @Email(message = "Must be a correctly formatted email address")
    @NotBlank(message = "The email is required")
    private String email;

    /**
     * The password of the user. It cannot be null or blank.
     */
    @NotBlank(message = "The password is required")
    private String password;
}