package com.alerom.rng.archive.rng_archive_server.exceptions;

/**
 * Custom exception thrown when a user cannot be found in the database.
 * This is used during the login process when the provided credentials do not
 * match an existing user.
 *
 * @author Alejo Romero
 * @version 1.0
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}