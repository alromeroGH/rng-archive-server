package com.alerom.rng.archive.rng_archive_server.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for user registration requests.
 * It contains the necessary information to create a new user account.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@Getter
@Setter
public class RegisterRequest {

    /**
     * The username of the new user. It cannot be null or blank.
     */
    @NotBlank(message = "The username is required")
    private String userName;

    /**
     * The email address of the new user. It must be a valid email format and cannot be null or blank.
     */
    @Email(message = "Must be a correctly formatted email address")
    @NotBlank(message = "The email is required")
    private String email;

    /**
     * The password for the new user account. It cannot be null or blank.
     */
    @NotBlank(message = "The password is required")
    private String password;

    /**
     * The unique identifier (UID) of the user. It cannot be null or blank.
     */
    @NotBlank(message = "The UID is required")
    protected String uid;
}