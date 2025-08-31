package com.alerom.rng.archive.rng_archive_server.exceptions;

public class NewsNotFoundException extends RuntimeException {
    public NewsNotFoundException(String message) {
        super(message);
    }
}