package com.alerom.rng.archive.rng_archive_server.exceptions;

/**
 * Custom exception thrown when a user attempts to register with an email
 * that is already in use.
 * This helps to provide a specific and clear error message to the client.
 *
 * @author Alejo Romero
 * @version 1.0
 */
public class EmailAlreadyExistException extends RuntimeException {
    public EmailAlreadyExistException(String message) {
        super(message);
    }
}