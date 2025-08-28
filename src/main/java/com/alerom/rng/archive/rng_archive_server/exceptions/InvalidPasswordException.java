package com.alerom.rng.archive.rng_archive_server.exceptions;

/**
 * Custom exception thrown when a password does not meet the required criteria.
 * This can be used to handle various password-related validation failures,
 * such as being too short, not containing a number, or lacking a special character.
 *
 * @author Alejo Romero
 * @version 1.0
 */
public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}