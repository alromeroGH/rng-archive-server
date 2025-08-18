package com.alerom.rng.archive.rng_archive_server.exceptions;

/**
 * Custom exception thrown when a user attempts to register with a UID
 * that is already in use.
 * This helps to handle the specific case of a UID conflict during registration.
 *
 * @author Alejo Romero
 * @version 1.0
 */
public class UidAlreadyExistException extends RuntimeException {
    public UidAlreadyExistException(String message) {
        super(message);
    }
}