package com.alerom.rng.archive.rng_archive_server.exceptions;

public class PullNotFoundException extends RuntimeException {
    public PullNotFoundException(String message) {
        super(message);
    }
}